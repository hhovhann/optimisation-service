package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.exception.CampaignNotFoundException;
import com.hhovhann.optimisationservice.exception.OptimisationNotFoundException;
import com.hhovhann.optimisationservice.mapper.OptimisationMapper;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.hhovhann.optimisationservice.model.OptimisationStatus.APPLIED;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Service
public class OptimisationServiceImpl implements OptimisationService {

    private final OptimisationRepository optimisationRepository;
    private final OptimisationMapper optimisationMapper;
    private final CampaignRepository campaignRepository;
    private final RecommendationService recommendationService;
    private final CampaignService campaignService;

    public OptimisationServiceImpl(OptimisationRepository optimisationRepository, OptimisationMapper optimisationMapper, CampaignRepository campaignRepository, RecommendationService recommendationService, CampaignService campaignService) {
        this.optimisationRepository = optimisationRepository;
        this.optimisationMapper = optimisationMapper;
        this.campaignRepository = campaignRepository;
        this.recommendationService = recommendationService;
        this.campaignService = campaignService;
    }

    @Override
    public OptimisationDto getOptimisation(Long optimisationId) {
        Optimisation optimisation = optimisationRepository
                .findById(optimisationId)
                .orElseThrow(() -> new OptimisationNotFoundException("No optimisation found by provided id"));
        return optimisationMapper.toDto(singletonList(optimisation)).get(0);
    }

    @Override
    public OptimisationDto getLatestOptimisationForCampaignGroup(Long campaignGroupId) {
        List<Optimisation> optimisations = optimisationRepository.findByCampaignGroupIdOrderByIdDesc(campaignGroupId);
        if (optimisations.isEmpty()) {
            throw new OptimisationNotFoundException("No optimisation found by provided id");
        }
        return optimisationMapper.toDto(optimisations).get(0);
    }

    public List<RecommendationDto> getLatestRecommendations(Long optimisationId) {
        Optimisation optimisation = this.optimisationRepository.findById(optimisationId)
                .orElseThrow(() -> new OptimisationNotFoundException("No optimisation found by provided id"));

        List<Campaign> campaigns = this.campaignRepository.findByCampaignGroupId(optimisation.getCampaignGroupId());
        if (campaigns.isEmpty()) {
            throw new CampaignNotFoundException("No campaign found by provided id");
        }

        if (Objects.equals(APPLIED.name(), optimisation.getStatus().name())) {
            return emptyList();
        }

        return generateLatestRecommendations(campaigns, optimisation);
    }

    public List<RecommendationDto> generateLatestRecommendations(List<Campaign> campaigns, Optimisation optimisation) {
        BigDecimal impressions = BigDecimal.valueOf(campaigns.stream().mapToDouble(Campaign::getImpressions).sum());
        BigDecimal budgets = campaigns.stream().map(Campaign::getBudget).reduce(BigDecimal.ZERO, BigDecimal::add);

        return campaigns.stream().map(
                currentCampaign -> new RecommendationDto(null, currentCampaign.getId(), optimisation.getId(), calculateRecommendedBudget(budgets, BigDecimal.valueOf(currentCampaign.getImpressions()), impressions))
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int applyRecommendations(List<RecommendationDto> recommendations, OptimisationDto optimisationDto) {
        AtomicInteger rowsUpdated = new AtomicInteger();
        recommendations.forEach(recommendation ->
                rowsUpdated.set(rowsUpdated.get() + campaignService.updateCampaign(recommendation.campaignId(), recommendation.recommendedBudget())));

        recommendationService.storeRecommendations(recommendations);

        optimisationRepository.save(new Optimisation(optimisationDto.id(), optimisationDto.campaignGroupId(), APPLIED));

        return rowsUpdated.get();
    }

    private BigDecimal calculateRecommendedBudget(BigDecimal sumOfBudgets, BigDecimal campaignImpressions, BigDecimal sumOfImpressions) {
        return sumOfBudgets.multiply(campaignImpressions.divide(sumOfImpressions, RoundingMode.HALF_DOWN));
    }
}

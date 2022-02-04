package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.exception.CampaignNotFoundException;
import com.hhovhann.optimisationservice.exception.OptimisationNotFoundException;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.hhovhann.optimisationservice.model.OptimisationStatus.APPLIED;
import static java.util.Collections.emptyList;

@Service
public class OptimisationServiceImpl implements OptimisationService {

    private final OptimisationRepository optimisationRepository;
    private final CampaignRepository campaignRepository;
    private final RecommendationService recommendationService;
    private final CampaignService campaignService;

    public OptimisationServiceImpl(OptimisationRepository optimisationRepository, CampaignRepository campaignRepository, RecommendationService recommendationService, CampaignService campaignService) {
        this.optimisationRepository = optimisationRepository;
        this.campaignRepository = campaignRepository;
        this.recommendationService = recommendationService;
        this.campaignService = campaignService;
    }

    @Override
    public OptimisationDto getOptimisation(Long optimisationId) {
        return optimisationRepository
                .findOptimisationDtoById_Named(optimisationId)
                .orElseThrow(() -> new OptimisationNotFoundException("No optimisation found by provided id"));
    }

    @Override
    public OptimisationDto getLatestOptimisationForCampaignGroup(Long campaignGroupId) {
        List<OptimisationDto> optimisations = optimisationRepository.findOptimisationDtoByCampaignGroupIdOrderByIdDesc_Named(campaignGroupId);
        if (optimisations.isEmpty()) {
            throw new OptimisationNotFoundException("No optimisation found by provided id");
        }
        return optimisations.get(0);
    }

    public List<RecommendationDto> getLatestRecommendations(Long optimisationId) {
        OptimisationDto optimisation = this.optimisationRepository.findOptimisationDtoById_Named(optimisationId)
                .orElseThrow(() -> new OptimisationNotFoundException("No optimisation found by provided id"));

        List<CampaignDto> campaigns = this.campaignRepository.findByCampaignGroupId(optimisation.campaignGroupId());
        if (campaigns.isEmpty()) {
            throw new CampaignNotFoundException("No campaign found by provided id");
        }


        if (Objects.equals(APPLIED.name(), optimisation.status())) {
            return emptyList();
        }

        return generateLatestRecommendations(campaigns, optimisation);
    }

    public List<RecommendationDto> generateLatestRecommendations(List<CampaignDto> campaign, OptimisationDto optimisation) {
        double impressions = campaign.stream().mapToDouble(CampaignDto::impressions).sum();
        BigDecimal budgets = campaign.stream().map(CampaignDto::budget).reduce(BigDecimal.ZERO, BigDecimal::add);

        return campaign.stream().map(
                currentCampaign -> new RecommendationDto(null, currentCampaign.id(), optimisation.id(), calculateRecommendedBudget(budgets, currentCampaign.impressions(), impressions))
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

    private BigDecimal calculateRecommendedBudget(BigDecimal sumOfBudgets, double campaignImpressions, double sumOfImpressions) {
        return sumOfBudgets.multiply(BigDecimal.valueOf(campaignImpressions / sumOfImpressions));
    }
}

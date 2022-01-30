package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    public OptimisationServiceImpl(OptimisationRepository optimisationRepository, CampaignRepository campaignRepository, RecommendationService recommendationService, CampaignService campaignService) {
        this.optimisationRepository = optimisationRepository;
        this.campaignRepository = campaignRepository;
        this.recommendationService = recommendationService;
        this.campaignService = campaignService;
    }

    @Override
    public Optional<OptimisationDto> getOptimisation(Long optimisationId) {
        return optimisationRepository.findOptimisationDtoById_Named(optimisationId);
    }

    @Override
    public Optional<OptimisationDto> getLatestOptimisationForCampaignGroup(Long campaignGroupId) {
        List<OptimisationDto> optimisations = optimisationRepository.findOptimisationDtoByCampaignGroupIdOrderByIdDesc_Named(campaignGroupId);
        return Optional.of(optimisations.get(0));
    }

    public List<Recommendation> getLatestRecommendations(Long optimisationId) {
        Optional<OptimisationDto> optimisationOptional = this.optimisationRepository.findOptimisationDtoById_Named(optimisationId);
        if (optimisationOptional.isEmpty() || optimisationOptional.get().status().equals(APPLIED)) {
            return emptyList();
        }

        OptimisationDto optimisation = optimisationOptional.get();
        List<CampaignDto> campaigns = this.campaignRepository.findByCampaignGroupId(optimisation.campaignGroupId());
        if (campaigns.isEmpty()) {
            return emptyList();
        }

        return generateLatestRecommendations(campaigns, optimisation);
    }

    public List<Recommendation> generateLatestRecommendations(List<CampaignDto> campaign, OptimisationDto optimisation) {
        double impressions = campaign.stream().mapToDouble(CampaignDto::impressions).sum();
        BigDecimal budgets = campaign.stream().map(CampaignDto::budget).reduce(BigDecimal.ZERO, BigDecimal::add);

        return campaign.stream()
                .map(currentCampaign -> Recommendation.builder()
                        .campaignId(currentCampaign.id())
                        .optimisationId(optimisation.id())
                        .recommendedBudget(calculateRecommendedBudget(budgets, currentCampaign.impressions(), impressions))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int applyRecommendations(List<Recommendation> recommendations, OptimisationDto optimisationDto) {
        AtomicInteger rowsUpdated = new AtomicInteger();
        recommendations.forEach(recommendation ->
                rowsUpdated.set(rowsUpdated.get() + campaignService.updateCampaign(recommendation.getCampaignId(), recommendation.getRecommendedBudget())));

        recommendationService.storeRecommendations(recommendations);

        optimisationRepository.save(new Optimisation(optimisationDto.id(), optimisationDto.campaignGroupId(), APPLIED));

        return rowsUpdated.get();
    }

    private BigDecimal calculateRecommendedBudget(BigDecimal sumOfBudgets, double campaignImpressions, double sumOfImpressions) {
        return sumOfBudgets.multiply(BigDecimal.valueOf(campaignImpressions / sumOfImpressions));
    }
}

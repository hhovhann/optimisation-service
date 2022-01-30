package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.CampaignDto;
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
    public Optional<Optimisation> getOptimisation(Long optimisationId) {
        return optimisationRepository.findById(optimisationId);
    }

    @Override
    public Optional<Optimisation> getLatestOptimisationForCampaignGroup(Long campaignGroupId) {
        List<Optimisation> optimisations = optimisationRepository.findByCampaignGroupIdOrderByIdDesc(campaignGroupId);
        return Optional.of(optimisations.get(0));
    }

    public List<Recommendation> getLatestRecommendations(Long optimisationId) {
        Optional<Optimisation> optimisationOptional = this.optimisationRepository.findById(optimisationId);
        if (optimisationOptional.isEmpty() || optimisationOptional.get().getStatus().equals(APPLIED)) {
            return emptyList();
        }

        Optimisation optimisation = optimisationOptional.get();
        List<CampaignDto> campaigns = this.campaignRepository.findByCampaignGroupId(optimisation.getCampaignGroupId());
        if (campaigns.isEmpty()) {
            return emptyList();
        }

        return generateLatestRecommendations(campaigns, optimisation);
    }

    public List<Recommendation> generateLatestRecommendations(List<CampaignDto> campaign, Optimisation optimisation) {
        double impressions = campaign.stream().mapToDouble(CampaignDto::impressions).sum();
        BigDecimal budgets = campaign.stream().map(CampaignDto::budget).reduce(BigDecimal.ZERO, BigDecimal::add);

        return campaign.stream()
                .map(currentCampaign -> Recommendation.builder()
                        .campaignId(currentCampaign.id())
                        .optimisationId(optimisation.getId())
                        .recommendedBudget(calculateRecommendedBudget(budgets, currentCampaign.impressions(), impressions))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int applyRecommendations(List<Recommendation> recommendations, Optimisation optimisation) {
        AtomicInteger rowsUpdated = new AtomicInteger();
        recommendations.forEach(recommendation ->
                rowsUpdated.set(rowsUpdated.get() + campaignService.updateCampaign(recommendation.getCampaignId(), recommendation.getRecommendedBudget())));

        recommendationService.storeRecommendations(recommendations);

        storeOptimisationStatus(optimisation);

        return rowsUpdated.get();
    }

    private void storeOptimisationStatus(Optimisation optimisation) {
        optimisation.setStatus(APPLIED);
        this.optimisationRepository.save(optimisation);
    }

    private BigDecimal calculateRecommendedBudget(BigDecimal sumOfBudgets, double campaignImpressions, double sumOfImpressions) {
        return sumOfBudgets.multiply(BigDecimal.valueOf(campaignImpressions / sumOfImpressions));
    }
}

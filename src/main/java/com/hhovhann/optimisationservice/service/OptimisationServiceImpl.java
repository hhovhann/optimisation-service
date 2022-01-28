package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.hhovhann.optimisationservice.model.entity.OptimisationStatus.APPLIED;

@Service
public class OptimisationServiceImpl implements OptimisationService {


    private final OptimisationRepository optimisationRepository;
    private final CampaignRepository campaignRepository;
    private final RecommendationService recommendationService;
    private final CampaignService campaignService;

    @Autowired
    public OptimisationServiceImpl(OptimisationRepository optimisationRepository,
                                   CampaignRepository campaignRepository, RecommendationService recommendationService, CampaignService campaignService) {
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
        Optional<Optimisation> optimisation = this.optimisationRepository.findById(optimisationId);
        if (optimisation.isEmpty() || optimisation.get().getStatus().equals(APPLIED)) {
            return Collections.emptyList();
        }

        List<Campaign> campaigns = this.campaignRepository.findByCampaignGroupId(optimisation.get().getCampaignGroupId());
        if (campaigns.isEmpty()) {
            return Collections.emptyList();
        }

        return generateLatestRecommendations(campaigns, optimisation.get());
    }

    public List<Recommendation> generateLatestRecommendations(List<Campaign> campaign, Optimisation optimisation) {
        double impressions = campaign.stream().mapToDouble(Campaign::getImpressions).sum();
        BigDecimal budgets = campaign.stream().map(Campaign::getBudget).reduce(BigDecimal.ZERO, BigDecimal::add);

        return campaign
                .stream()
                .map(currentCampaign -> Recommendation.builder()
                        .campaignId(currentCampaign.getId())
                        .optimisationId(optimisation.getId())
                        .recommendedBudget(budgets.multiply(BigDecimal.valueOf(currentCampaign.getImpressions() / impressions)))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int applyRecommendations(List<Recommendation> recommendations, Optimisation optimisation) {
        AtomicInteger rowsUpdated = new AtomicInteger();
        recommendations.forEach(recommendation -> rowsUpdated.set(rowsUpdated.get() + campaignService.updateCampaign(recommendation.getCampaignId(), recommendation.getRecommendedBudget())));

        recommendationService.storeRecommendations(recommendations);

        optimisation.setStatus(APPLIED);

        this.optimisationRepository.save(optimisation);

        return rowsUpdated.get();
    }
}

package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.OptimisationStatus;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import com.hhovhann.optimisationservice.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OptimisationServiceImpl implements OptimisationService {


    private final OptimisationRepository optimisationRepository;
    private final RecommendationRepository recommendationRepository;
    private final CampaignRepository campaignRepository;

    @Autowired
    public OptimisationServiceImpl(OptimisationRepository optimisationRepository,
                                   RecommendationRepository recommendationRepository,
                                   CampaignRepository campaignRepository) {
        this.optimisationRepository = optimisationRepository;
        this.recommendationRepository = recommendationRepository;
        this.campaignRepository = campaignRepository;
    }


    @Override
    public Optional<Optimisation> getLatestOptimisation(Long campaignGroupId) {
        List<Optimisation> optimisations = this.optimisationRepository.findByCampaignGroupIdOrderByIdDesc(campaignGroupId);
        return Optional.of(optimisations.get(0));
    }

    public List<Recommendation> getLatestRecommendations(Long optimisationId) {
        Optional<Optimisation> optimisation = this.optimisationRepository.findById(optimisationId);
        if (optimisation.isEmpty() || optimisation.get().getStatus().equals(OptimisationStatus.APPLIED)) {
            return Collections.emptyList();
        }

        List<Campaign> campaigns = this.campaignRepository.findByCampaignGroupId(optimisation.get().getCampaignGroupId());
        if (campaigns.isEmpty()) {
            return Collections.emptyList();
        }

        return this.generateLatestRecommendations(campaigns, optimisation.get());
    }

    public List<Recommendation> generateLatestRecommendations(List<Campaign> campaign, Optimisation optimisation) {

        final Double sumImpressions = campaign.stream().mapToDouble(Campaign::getImpressions).sum();
        final BigDecimal sumTotalBudget = campaign.stream().map(Campaign::getBudget).reduce(BigDecimal.ZERO, BigDecimal::add);


        return campaign
                .stream()
                .map(c -> Recommendation.builder()
                        .campaignId(c.getId())
                        .optimisationId(optimisation.getId())
                        .recommendedBudget(sumTotalBudget.multiply(BigDecimal.valueOf(c.getImpressions() / sumImpressions)))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public int applyRecommendations(List<Recommendation> recommendations, Optimisation optimisation) {

        var rowsUpdated = 0;
        for (Recommendation recommendation : recommendations) {
            rowsUpdated = rowsUpdated + this.campaignRepository.updateCampaign(recommendation.getCampaignId(), recommendation.getRecommendedBudget());

        }

        this.recommendationRepository.saveAll(recommendations);

        optimisation.setStatus(OptimisationStatus.APPLIED);
        this.optimisationRepository.save(optimisation);
        return rowsUpdated;
    }

    @Override
    public int applyLatestRecommendation(Long optimisationId) {
        Optional<Optimisation> optimisation = this.optimisationRepository.findById(optimisationId);


//        if (optimisation.isEmpty()) {
//            return ResponseEntity.notFound().headers(headers).build();
//        }
//
//        if (optimisation.get().getStatus().equals(OptimisationStatus.APPLIED)) {
//            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).headers(headers).build();
//        }

        List<Recommendation> recommendations = getLatestRecommendations(optimisationId);
        return applyRecommendations(recommendations, optimisation.get());

    }
}

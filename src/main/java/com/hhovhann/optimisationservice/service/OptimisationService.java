package com.hhovhann.optimisationservice.service;

import java.util.List;
import java.util.Optional;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;

public interface OptimisationService {

    Optional<Optimisation> getOptimisation(Long optimisationId);

    Optional<Optimisation> getLatestOptimisationForCampaignGroup(Long campaignGroupId);

    List<Recommendation> getLatestRecommendations(Long optimisationId);

    int applyRecommendations(List<Recommendation> recommendations, Optimisation optimisation);
}

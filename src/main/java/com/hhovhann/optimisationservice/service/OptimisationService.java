package com.hhovhann.optimisationservice.service;

import java.util.List;
import java.util.Optional;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;

public interface OptimisationService {

    Optional<Optimisation> getOptimisationByOptimisationId(Long optimisationId);

    Optional<Optimisation> getLatestOptimisation(Long campaignGroupId);

    List<Recommendation> getLatestRecommendations(Long optimisationId);

    int applyRecommendations(List<Recommendation> recommendations, Optimisation optimisation);
}

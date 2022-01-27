package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;

import java.util.List;
import java.util.Optional;

public interface OptimisationService {

    Optional<Optimisation> getLatestOptimisation(Long campaignGroupId);

    List<Recommendation> getLatestRecommendations(Long optimisationId);

    int applyRecommendations(List<Recommendation> recommendations, Optimisation optimisation);

    int applyLatestRecommendation(Long optimisationId);
}

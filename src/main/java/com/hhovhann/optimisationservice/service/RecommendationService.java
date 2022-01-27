package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Recommendation;

import java.util.List;

public interface RecommendationService {
    void storeRecommendations(List<Recommendation> recommendations);
}

package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.RecommendationDto;

import java.util.List;

public interface RecommendationService {
    void storeRecommendations(List<RecommendationDto> recommendations);
}

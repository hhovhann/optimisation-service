package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;

import java.util.List;
import java.util.Optional;

public interface OptimisationService {

    Optional<OptimisationDto> getOptimisation(Long optimisationId);

    Optional<OptimisationDto> getLatestOptimisationForCampaignGroup(Long campaignGroupId);

    List<RecommendationDto> getLatestRecommendations(Long optimisationId);

    int applyRecommendations(List<RecommendationDto> recommendations, OptimisationDto optimisation);
}

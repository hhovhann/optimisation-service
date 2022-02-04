package com.hhovhann.optimisationservice.service;

import java.util.List;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;

public interface OptimisationService {

    OptimisationDto getOptimisation(Long optimisationId);

    OptimisationDto getLatestOptimisationForCampaignGroup(Long campaignGroupId);

    List<RecommendationDto> getLatestRecommendations(Long optimisationId);

    int applyRecommendations(List<RecommendationDto> recommendations, OptimisationDto optimisation);
}

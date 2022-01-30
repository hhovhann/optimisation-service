package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.entity.Recommendation;

import java.util.List;
import java.util.Optional;

public interface OptimisationService {

    Optional<OptimisationDto> getOptimisation(Long optimisationId);

    Optional<OptimisationDto> getLatestOptimisationForCampaignGroup(Long campaignGroupId);

    List<Recommendation> getLatestRecommendations(Long optimisationId);

    int applyRecommendations(List<Recommendation> recommendations, OptimisationDto optimisation);
}

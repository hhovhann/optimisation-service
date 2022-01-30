package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public void storeRecommendations(List<RecommendationDto> recommendations) {
        List<Recommendation> recommendationList = recommendations
                .stream()
                .map(recommendationDto -> Recommendation.builder()
                        .id(recommendationDto.id())
                        .campaignId(recommendationDto.campaignId())
                        .optimisationId(recommendationDto.optimisationId())
                        .recommendedBudget(recommendationDto.recommendedBudget())
                        .build())
                .collect(Collectors.toList());
        this.recommendationRepository.saveAll(recommendationList);
    }
}

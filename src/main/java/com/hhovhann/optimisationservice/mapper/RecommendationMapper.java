package com.hhovhann.optimisationservice.mapper;

import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecommendationMapper {

    public List<Recommendation> toEntity(List<RecommendationDto> recommendationDtos) {
        return recommendationDtos
                .stream()
                .map(recommendationDto -> new Recommendation(recommendationDto.id(), recommendationDto.campaignId(), recommendationDto.optimisationId(), recommendationDto.recommendedBudget()))
                .collect(Collectors.toList());
    }
}

package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.mapper.RecommendationMapper;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    @Override
    public void storeRecommendations(List<RecommendationDto> recommendationDtos) {
        this.recommendationRepository.saveAll(recommendationMapper.toEntity(recommendationDtos));
    }
}

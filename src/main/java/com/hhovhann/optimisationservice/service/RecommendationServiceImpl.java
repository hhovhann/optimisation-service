package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public List<Recommendation> getRecommendations(Long optimisationId) {
        return recommendationRepository.findByOptimisationId(optimisationId);
    }

    @Override
    public void storeRecommendations(List<Recommendation> recommendations) {
        this.recommendationRepository.saveAll(recommendations);
    }
}

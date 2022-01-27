package com.hhovhann.optimisationservice.repository;

import com.hhovhann.optimisationservice.model.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByOptimisationId(Long optimisationId);
}

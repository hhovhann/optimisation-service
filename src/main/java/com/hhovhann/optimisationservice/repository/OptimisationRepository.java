package com.hhovhann.optimisationservice.repository;

import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptimisationRepository extends JpaRepository<Optimisation, Long> {

    @Query(nativeQuery = true)
    Optional<OptimisationDto> findOptimisationDtoById_Named(Long id);

    List<Optimisation> findByCampaignGroupIdOrderByIdDesc(Long campaignGroupId);
}

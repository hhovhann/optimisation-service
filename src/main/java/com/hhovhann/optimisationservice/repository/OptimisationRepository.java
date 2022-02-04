package com.hhovhann.optimisationservice.repository;

import com.hhovhann.optimisationservice.model.entity.Optimisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptimisationRepository extends JpaRepository<Optimisation, Long> {

    @Override
    Optional<Optimisation> findById(Long aLong);

    List<Optimisation> findByCampaignGroupIdOrderByIdDesc(Long campaignGroupId);

}

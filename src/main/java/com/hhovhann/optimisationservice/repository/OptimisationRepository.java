package com.hhovhann.optimisationservice.repository;

import com.hhovhann.optimisationservice.model.entity.Optimisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptimisationRepository extends JpaRepository<Optimisation, Long> {

    List<Optimisation> findByCampaignGroupIdOrderByIdDesc(Long campaignGroupId);
}

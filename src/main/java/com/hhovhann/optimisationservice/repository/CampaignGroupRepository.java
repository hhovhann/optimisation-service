package com.hhovhann.optimisationservice.repository;

import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignGroupRepository extends JpaRepository<CampaignGroup, Long> {
}

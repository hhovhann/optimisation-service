package com.hhovhann.optimisationservice.repository;

import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignGroupRepository extends JpaRepository<CampaignGroup, Long> {
    @Query(nativeQuery = true)
    List<CampaignGroupDto> findAllCampaignGroupDto_Named();
}

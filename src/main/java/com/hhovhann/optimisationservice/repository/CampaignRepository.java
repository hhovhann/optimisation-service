package com.hhovhann.optimisationservice.repository;

import com.hhovhann.optimisationservice.model.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByCampaignGroupId(Long campaignGroupId);

    @Modifying
    @Transactional
    @Query("UPDATE Campaign cmp SET cmp.budget = :budget WHERE cmp.id = :campaignId")
    int updateCampaign(@Param("campaignId") Long campaignId, @Param("budget") BigDecimal budget);
}

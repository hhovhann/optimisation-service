package com.hhovhann.optimisationservice.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.entity.Campaign;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DBRider
@SpringBootTest
@DBUnit(cacheConnection = false, leakHunter = true)
class CampaignRepositoryTest {

    @Autowired
    private CampaignRepository campaignRepository;

    @Test
    @DataSet("datasets/campaign/campaigns.yml")
    void findByCampaignGroupId() {
        List<Campaign> campaigns = campaignRepository.findByCampaignGroupId(1L);

        assertEquals(campaigns.size(), 2);
        assertEquals(campaigns.get(0).getCampaignGroupId(), 1);
        assertEquals(campaigns.get(1).getCampaignGroupId(), 1);
    }

    @Test
    @DataSet(value = "datasets/campaign/campaign.yml")
    void updateCampaign() {
        campaignRepository.updateCampaign(1L, BigDecimal.valueOf(70));

        Optional<Campaign> actualCampaignOptional = campaignRepository.findById(1L);

        assertEquals(actualCampaignOptional.get().getBudget(), BigDecimal.valueOf(70));
    }
}
package com.hhovhann.optimisationservice.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DBRider
@SpringBootTest
@DBUnit(cacheConnection = false, leakHunter = true)
class CampaignGroupRepositoryTest {

    @Autowired
    private CampaignGroupRepository campaignGroupRepository;

    @Test
    @DataSet(value = {"datasets/campaignGroup/campaignGroup.yml", "datasets/campaign/campaign.yml", "datasets/optimisation/optimisation.yml"})
    void shouldReturnAllCampaignGroupDto() {
        List<CampaignGroup> campaignGroupDtos = campaignGroupRepository.findAll();

        assertEquals(campaignGroupDtos.size(), 1);
        assertEquals(campaignGroupDtos.get(0).getId(), 1);
        assertEquals(campaignGroupDtos.get(0).getName(), "firstCampaigns");
    }
}
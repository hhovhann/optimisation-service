package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OptimisationServiceTest {
    @Autowired
    private OptimisationService optimisationService;

    private Optimisation optimisation;
    private Recommendation recommendationOne;
    private Recommendation recommendationTwo;
    private Campaign campaignOne;
    private Campaign campaignTwo;
    private CampaignGroup campaignGroup;

    @BeforeEach
    public void setup() {
        this.campaignGroup = CampaignGroup.builder()
                .id(1L)
                .name("Campaign Group One").build();

        this.campaignOne = Campaign.builder()
                .id(1L)
                .campaignGroupId(this.campaignGroup.getId())
                .budget(BigDecimal.TEN)
                .impressions(10D)
                .name("Fist Campaign")
                .revenue(BigDecimal.TEN)
                .build();

        this.campaignTwo = Campaign.builder()
                .id(2L)
                .campaignGroupId(this.campaignGroup.getId())
                .budget(BigDecimal.TEN)
                .impressions(40D)
                .name("Second Campaign")
                .revenue(BigDecimal.TEN)
                .build();

        this.optimisation = Optimisation.builder()
                .id(1L)
                .campaignGroupId(this.campaignGroup.getId())
                .status(OptimisationStatus.NOT_APPLIED)
                .build();

        this.recommendationOne = Recommendation.builder()
                .campaignId(this.campaignOne.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(4D)).build();

        this.recommendationTwo = Recommendation.builder()
                .campaignId(this.campaignTwo.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();
    }

    @Test
    void getOptimisation() {
    }

    @Test
    void getLatestOptimisationForCampaignGroup() {
    }

    @Test // givenCampaigns_whenGenerateLatestRecommendations_thenReturnRecommendations
    void getLatestRecommendations() {
//        List<Campaign> campaigns = new ArrayList<>();
//        campaigns.add(this.campaignOne);
//        campaigns.add(this.campaignTwo);
//
//        List<Recommendation> expectedRecommendations = new ArrayList<>();
//        expectedRecommendations.add(this.recommendationOne);
//        expectedRecommendations.add(this.recommendationTwo);
//
//
//        List<Recommendation> outputRecommendations = this.optimisationService.applyRecommendations(campaigns, this.optimisation);
//        assertEquals(outputRecommendations.size(), expectedRecommendations.size());
//        assertEquals(outputRecommendations.get(0).getRecommendedBudget(), expectedRecommendations.get(0).getRecommendedBudget());
//        assertEquals(outputRecommendations.get(0).getOptimisationId(), expectedRecommendations.get(0).getOptimisationId());
//        assertEquals(outputRecommendations.get(0).getCampaignId(), expectedRecommendations.get(0).getCampaignId());
//
//        assertEquals(outputRecommendations.get(1).getRecommendedBudget(), expectedRecommendations.get(1).getRecommendedBudget());
//        assertEquals(outputRecommendations.get(1).getOptimisationId(), expectedRecommendations.get(1).getOptimisationId());
//        assertEquals(outputRecommendations.get(1).getCampaignId(), expectedRecommendations.get(1).getCampaignId());
    }

    @Test
    void applyRecommendations() {
    }
}
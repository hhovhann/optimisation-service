package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Updated raws by count of given recommendations when optimisation status not applied")
    void givenRecommendations_whenApplyLatestRecommendations_thenReturnUpdatedRaws() {
        List<Recommendation> expectedRecommendations = new ArrayList<>();
        expectedRecommendations.add(this.recommendationOne);
        expectedRecommendations.add(this.recommendationTwo);

        int updatedRaws = this.optimisationService.applyRecommendations(expectedRecommendations, this.optimisation);

        assertEquals(updatedRaws, expectedRecommendations.size());
    }
}
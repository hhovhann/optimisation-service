package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.hhovhann.optimisationservice.model.entity.OptimisationStatus.NOT_APPLIED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OptimisationServiceTest {
    @Autowired
    private OptimisationService optimisationService;

    private Optimisation optimisation;
    private Recommendation recommendation_1, recommendation_2, recommendation_3, recommendation_4, recommendation_5, recommendation_6, recommendation_7, recommendation_8, recommendation_9, recommendation_10, recommendation_11;
    private Campaign campaign_1, campaign_2;
    private CampaignGroup campaignGroup;

    @BeforeEach
    public void setup() {
        this.campaignGroup = CampaignGroup.builder()
                .id(1L)
                .name("Campaign Group One").build();

        this.campaign_1 = Campaign.builder()
                .id(1L)
                .campaignGroupId(this.campaignGroup.getId())
                .budget(BigDecimal.TEN)
                .impressions(10D)
                .name("Fist Campaign")
                .revenue(BigDecimal.TEN).build();


        this.campaign_2 = Campaign.builder()
                .id(2L)
                .campaignGroupId(this.campaignGroup.getId())
                .budget(BigDecimal.TEN)
                .impressions(40D)
                .name("Second Campaign")
                .revenue(BigDecimal.TEN).build();


        this.optimisation = Optimisation.builder().id(1L).campaignGroupId(this.campaignGroup.getId()).status(NOT_APPLIED).build();

        this.recommendation_1 = Recommendation.builder()
                .campaignId(this.campaign_1.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(4D)).build();

        this.recommendation_2 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_3 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_4 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_5 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_6 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_7 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_8 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_9 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_10 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();

        this.recommendation_11 = Recommendation.builder()
                .campaignId(this.campaign_2.getId())
                .optimisationId(this.optimisation.getId())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();
    }

    @Test
    @Order(1)
    @DisplayName("Retrieve optimisation when optimisation id is provided")
    void givenOptimisation_whenGetOptimisation_thenReturnExistingOptimisation() {
        Optimisation expectedOptimisation = this.optimisation;

        Optimisation actualOptimisation = this.optimisationService.getOptimisation(this.optimisation.getId()).get();

        assertEquals(expectedOptimisation.getId(), actualOptimisation.getId());
        assertEquals(expectedOptimisation.getCampaignGroupId(), actualOptimisation.getCampaignGroupId());
        assertEquals(expectedOptimisation.getStatus(), actualOptimisation.getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("Retrieve all recommendations when optimisation status not applied")
    void givenCampaignGroup_whenGetLatestOptimisationForCampaignGroup_thenReturnExistingOptimisation() {
        Optimisation expectedOptimisation = this.optimisation;

        Optimisation actualOptimisation = this.optimisationService.getLatestOptimisationForCampaignGroup(this.campaignGroup.getId()).get();

        assertEquals(expectedOptimisation.getId(), actualOptimisation.getId());
        assertEquals(expectedOptimisation.getCampaignGroupId(), actualOptimisation.getCampaignGroupId());
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve all recommendations when optimisation status not applied")
    void givenRecommendations_whenGetLatestRecommendations_thenReturnAllRecommendations() {
        List<Recommendation> expectedRecommendations = List.of(this.recommendation_1, this.recommendation_2, this.recommendation_3, this.recommendation_4, this.recommendation_5, this.recommendation_6, this.recommendation_7, this.recommendation_8, this.recommendation_9, this.recommendation_10, this.recommendation_11);

        List<Recommendation> actualRecommendations = this.optimisationService.getLatestRecommendations(this.optimisation.getId());

        assertEquals(expectedRecommendations.size(), actualRecommendations.size());
    }

    @Test
    @Order(4)
    @DisplayName("Updated raws by count of given recommendations when optimisation status not applied")
    void givenRecommendations_whenApplyLatestRecommendations_thenReturnUpdatedRaws() {
        List<Recommendation> expectedRecommendations = new ArrayList<>();
        expectedRecommendations.add(this.recommendation_1);
        expectedRecommendations.add(this.recommendation_2);

        int updatedRaws = this.optimisationService.applyRecommendations(expectedRecommendations, this.optimisation);

        assertEquals(updatedRaws, expectedRecommendations.size());
    }
}
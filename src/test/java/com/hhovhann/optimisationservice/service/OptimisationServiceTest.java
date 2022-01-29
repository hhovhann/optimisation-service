package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.hhovhann.optimisationservice.model.entity.OptimisationStatus.APPLIED;
import static com.hhovhann.optimisationservice.model.entity.OptimisationStatus.NOT_APPLIED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class OptimisationServiceTest {
    @Autowired
    private OptimisationService optimisationService;

    @MockBean
    CampaignRepository campaignRepository;

    @MockBean
    CampaignService campaignService;

    @MockBean
    RecommendationService recommendationService;

    @MockBean
    OptimisationRepository optimisationRepository;

    private Optimisation optimisation;
    private Recommendation recommendation_1, recommendation_2;
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
    }

    @Test
    @DisplayName("Retrieve optimisation when optimisation id is provided")
    void givenOptimisation_whenGetOptimisation_thenReturnExistingOptimisation() {
        Optimisation expectedOptimisation = this.optimisation;
        Mockito.when(optimisationRepository.findById(this.optimisation.getId())).thenReturn(Optional.of(this.optimisation));
        Optimisation actualOptimisation = this.optimisationService.getOptimisation(this.optimisation.getId()).get();

        assertEquals(expectedOptimisation.getId(), actualOptimisation.getId());
        assertEquals(expectedOptimisation.getCampaignGroupId(), actualOptimisation.getCampaignGroupId());
        assertEquals(expectedOptimisation.getStatus(), actualOptimisation.getStatus());
    }

    @Test
    @DisplayName("Retrieve all recommendations when optimisation status not applied")
    void givenCampaignGroup_whenGetLatestOptimisationForCampaignGroup_thenReturnExistingOptimisation() {
        Optimisation expectedOptimisation = this.optimisation;
        Mockito.when(optimisationRepository.findByCampaignGroupIdOrderByIdDesc(this.campaignGroup.getId())).thenReturn(Collections.singletonList(this.optimisation));

        Optimisation actualOptimisation = this.optimisationService.getLatestOptimisationForCampaignGroup(this.campaignGroup.getId()).get();

        assertEquals(expectedOptimisation.getId(), actualOptimisation.getId());
        assertEquals(expectedOptimisation.getCampaignGroupId(), actualOptimisation.getCampaignGroupId());
        assertEquals(expectedOptimisation.getStatus(), actualOptimisation.getStatus());
    }

    @Test
    @DisplayName("Retrieve all recommendations when optimisation status not applied")
    void givenRecommendations_whenGetLatestRecommendations_thenReturnAllRecommendations() {
        List<Recommendation> expectedRecommendations = List.of(this.recommendation_1, this.recommendation_2);
        Mockito.when(this.campaignRepository.findByCampaignGroupId(optimisation.getCampaignGroupId())).thenReturn(List.of(this.campaign_1, this.campaign_2));

        List<Recommendation> actualRecommendations = this.optimisationService.getLatestRecommendations(this.optimisation.getId());

        assertEquals(expectedRecommendations.size(), actualRecommendations.size());

        assertEquals(expectedRecommendations.get(0).getCampaignId(), actualRecommendations.get(0).getCampaignId());
        assertEquals(expectedRecommendations.get(0).getOptimisationId(), actualRecommendations.get(0).getOptimisationId());
        assertEquals(expectedRecommendations.get(0).getRecommendedBudget(), actualRecommendations.get(0).getRecommendedBudget());

        assertEquals(expectedRecommendations.get(1).getCampaignId(), actualRecommendations.get(1).getCampaignId());
        assertEquals(expectedRecommendations.get(1).getOptimisationId(), actualRecommendations.get(1).getOptimisationId());
        assertEquals(expectedRecommendations.get(1).getRecommendedBudget(), actualRecommendations.get(1).getRecommendedBudget());
    }

    @Test
    @DisplayName("Updated raws by count of given recommendations when optimisation status not applied")
    void givenRecommendations_whenApplyLatestRecommendations_thenReturnUpdatedRaws() {
        List<Recommendation> recommendations = List.of(this.recommendation_1, this.recommendation_2);
        Optimisation expectedOptimisation = this.optimisation;
        expectedOptimisation.setStatus(APPLIED);
        when(this.campaignService.updateCampaign(this.recommendation_1.getCampaignId(), this.recommendation_1.getRecommendedBudget())).thenReturn(1);
        when(this.campaignService.updateCampaign(this.recommendation_2.getCampaignId(), this.recommendation_2.getRecommendedBudget())).thenReturn(1);
        doNothing().when(this.recommendationService).storeRecommendations(recommendations);
        when(this.optimisationRepository.save(this.optimisation)).thenReturn(expectedOptimisation);

        int updatedRaws = this.optimisationService.applyRecommendations(recommendations, this.optimisation);

        assertEquals(updatedRaws, recommendations.size());

        verify(this.recommendationService, times(1)).storeRecommendations(recommendations);
    }
}
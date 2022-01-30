package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.OptimisationStatus;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
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

import static com.hhovhann.optimisationservice.model.OptimisationStatus.APPLIED;
import static com.hhovhann.optimisationservice.model.OptimisationStatus.NOT_APPLIED;
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
    private OptimisationDto optimisationDto;
    private Recommendation recommendation_1, recommendation_2;
    private CampaignDto campaign_1, campaign_2;
    private CampaignGroup campaignGroup;

    @BeforeEach
    public void setup() {
        this.campaignGroup = CampaignGroup.builder()
                .id(1L)
                .name("Campaign Group One").build();

        this.campaign_1 = new CampaignDto(1L, "Fist Campaign", this.campaignGroup.getId(), BigDecimal.TEN, 10D, BigDecimal.TEN);
        this.campaign_2 = new CampaignDto(2L, "Second Campaign", this.campaignGroup.getId(), BigDecimal.TEN, 40D, BigDecimal.TEN);

        this.optimisationDto = new OptimisationDto(1L, this.campaignGroup.getId(), APPLIED.name());
        this.optimisation = new Optimisation(this.optimisationDto.id(), this.optimisationDto.campaignGroupId(), OptimisationStatus.valueOf(this.optimisationDto.status()));
        this.recommendation_1 = Recommendation.builder()
                .campaignId(this.campaign_1.id())
                .optimisationId(this.optimisationDto.id())
                .recommendedBudget(BigDecimal.valueOf(4D)).build();

        this.recommendation_2 = Recommendation.builder()
                .campaignId(this.campaign_2.id())
                .optimisationId(this.optimisationDto.id())
                .recommendedBudget(BigDecimal.valueOf(16D)).build();
    }

    @Test
    @DisplayName("Retrieve optimisation when optimisation id is provided")
    void givenOptimisation_whenGetOptimisation_thenReturnExistingOptimisation() {
        OptimisationDto expectedOptimisation = this.optimisationDto;
        Mockito.when(optimisationRepository.findOptimisationDtoById_Named(this.optimisationDto.id())).thenReturn(Optional.of(this.optimisationDto));
        OptimisationDto actualOptimisation = this.optimisationService.getOptimisation(this.optimisationDto.id()).get();

        assertEquals(expectedOptimisation.id(), actualOptimisation.id());
        assertEquals(expectedOptimisation.campaignGroupId(), actualOptimisation.campaignGroupId());
        assertEquals(expectedOptimisation.status(), actualOptimisation.status());
    }

    @Test
    @DisplayName("Retrieve all recommendations when optimisation status not applied")
    void givenCampaignGroup_whenGetLatestOptimisationForCampaignGroup_thenReturnExistingOptimisation() {
        OptimisationDto expectedOptimisation = this.optimisationDto;
        Mockito.when(optimisationRepository.findOptimisationDtoByCampaignGroupIdOrderByIdDesc_Named(this.campaignGroup.getId())).thenReturn(Collections.singletonList(this.optimisationDto));

        OptimisationDto actualOptimisation = this.optimisationService.getLatestOptimisationForCampaignGroup(this.campaignGroup.getId()).get();

        assertEquals(expectedOptimisation.id(), actualOptimisation.id());
        assertEquals(expectedOptimisation.campaignGroupId(), actualOptimisation.campaignGroupId());
        assertEquals(expectedOptimisation.status(), actualOptimisation.status());
    }

    @Test
    @DisplayName("Retrieve all recommendations when optimisation status not applied")
    void givenRecommendations_whenGetLatestRecommendations_thenReturnAllRecommendations() {
        List<Recommendation> expectedRecommendations = List.of(this.recommendation_1, this.recommendation_2);
        Mockito.when(this.campaignRepository.findByCampaignGroupId(optimisationDto.campaignGroupId())).thenReturn(List.of(this.campaign_1, this.campaign_2));
        Mockito.when(optimisationRepository.findOptimisationDtoById_Named(this.optimisationDto.id())).thenReturn(Optional.of(this.optimisationDto));

        List<Recommendation> actualRecommendations = this.optimisationService.getLatestRecommendations(this.optimisationDto.id());

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
        Optimisation optimisation = this.optimisation;
        when(this.campaignService.updateCampaign(this.recommendation_1.getCampaignId(), this.recommendation_1.getRecommendedBudget())).thenReturn(1);
        when(this.campaignService.updateCampaign(this.recommendation_2.getCampaignId(), this.recommendation_2.getRecommendedBudget())).thenReturn(1);
        when(this.optimisationRepository.save(optimisation)).thenReturn(optimisation);
        doNothing().when(this.recommendationService).storeRecommendations(recommendations);

        int updatedRaws = this.optimisationService.applyRecommendations(recommendations, this.optimisationDto);

        assertEquals(updatedRaws, recommendations.size());

        verify(this.recommendationService, times(1)).storeRecommendations(recommendations);
    }
}
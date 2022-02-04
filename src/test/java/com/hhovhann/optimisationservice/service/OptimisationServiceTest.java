package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.OptimisationStatus;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
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

    private Optimisation optimisationWithStatusApplied;
    private Optimisation optimisationWithStatusNotApplied;
    private OptimisationDto optimisationDtoWithStatusApplied, optimisationDtoWithStatusNotApplied;
    private RecommendationDto recommendationDtoOne, recommendationDtoTwo;
    private CampaignDto campaignDtoOne, campaignDtoTwo;
    private CampaignGroupDto campaignGroup;

    @BeforeEach
    public void setup() {
        this.campaignGroup = new CampaignGroupDto(1L, "Campaign Group One");

        this.campaignDtoOne = new CampaignDto(1L, "Fist Campaign", this.campaignGroup.id(), BigDecimal.TEN, 10D, BigDecimal.TEN);
        this.campaignDtoTwo = new CampaignDto(2L, "Second Campaign", this.campaignGroup.id(), BigDecimal.TEN, 40D, BigDecimal.TEN);

        this.optimisationDtoWithStatusApplied = new OptimisationDto(1L, this.campaignGroup.id(), APPLIED.name());
        this.optimisationDtoWithStatusNotApplied = new OptimisationDto(2L, this.campaignGroup.id(), NOT_APPLIED.name());

        this.optimisationWithStatusApplied = new Optimisation(this.optimisationDtoWithStatusApplied.id(), this.optimisationDtoWithStatusApplied.campaignGroupId(), OptimisationStatus.valueOf(this.optimisationDtoWithStatusApplied.status()));
        this.optimisationWithStatusNotApplied = new Optimisation(this.optimisationDtoWithStatusNotApplied.id(), this.optimisationDtoWithStatusNotApplied.campaignGroupId(), OptimisationStatus.valueOf(this.optimisationDtoWithStatusNotApplied.status()));

        this.recommendationDtoOne = new RecommendationDto(1L, this.campaignDtoOne.id(), this.optimisationDtoWithStatusNotApplied.id(), BigDecimal.valueOf(4D));
        this.recommendationDtoTwo = new RecommendationDto(2L, this.campaignDtoTwo.id(), this.optimisationDtoWithStatusNotApplied.id(), BigDecimal.valueOf(16D));
    }

    @Test
    void givenOptimisation_whenGetOptimisation_thenReturnExistingOptimisation() {
        OptimisationDto expectedOptimisation = this.optimisationDtoWithStatusApplied;
        Mockito.when(optimisationRepository.findOptimisationDtoById_Named(this.optimisationDtoWithStatusApplied.id())).thenReturn(Optional.of(this.optimisationDtoWithStatusApplied));
        OptimisationDto actualOptimisation = this.optimisationService.getOptimisation(this.optimisationDtoWithStatusApplied.id());

        assertEquals(expectedOptimisation.id(), actualOptimisation.id());
        assertEquals(expectedOptimisation.campaignGroupId(), actualOptimisation.campaignGroupId());
        assertEquals(expectedOptimisation.status(), actualOptimisation.status());
    }

    @Test
    void givenCampaignGroup_whenGetLatestOptimisationForCampaignGroup_thenReturnExistingOptimisation() {
        OptimisationDto expectedOptimisation = this.optimisationDtoWithStatusApplied;
        Mockito.when(optimisationRepository.findOptimisationDtoByCampaignGroupIdOrderByIdDesc_Named(this.campaignGroup.id())).thenReturn(Collections.singletonList(this.optimisationDtoWithStatusApplied));

        OptimisationDto actualOptimisation = this.optimisationService.getLatestOptimisationForCampaignGroup(this.campaignGroup.id());

        assertEquals(expectedOptimisation.id(), actualOptimisation.id());
        assertEquals(expectedOptimisation.campaignGroupId(), actualOptimisation.campaignGroupId());
        assertEquals(expectedOptimisation.status(), actualOptimisation.status());
    }

    @Test
    @DisplayName("Retrieve all recommendations when optimisation status not applied")
    void givenRecommendations_whenGetLatestRecommendations_thenReturnAllRecommendations() {
        List<RecommendationDto> expectedRecommendations = List.of(this.recommendationDtoOne, this.recommendationDtoTwo);

        Mockito.when(this.campaignRepository.findByCampaignGroupId(optimisationDtoWithStatusNotApplied.campaignGroupId()))
                .thenReturn(List.of(this.campaignDtoOne, this.campaignDtoTwo));

        Mockito.when(optimisationRepository.findOptimisationDtoById_Named(this.optimisationDtoWithStatusNotApplied.id()))
                .thenReturn(Optional.of(this.optimisationDtoWithStatusNotApplied));

        List<RecommendationDto> actualRecommendations = this.optimisationService.getLatestRecommendations(this.optimisationDtoWithStatusNotApplied.id());

        assertEquals(expectedRecommendations.get(0).campaignId(), actualRecommendations.get(0).campaignId());
        assertEquals(expectedRecommendations.get(0).optimisationId(), actualRecommendations.get(0).optimisationId());
        assertEquals(expectedRecommendations.get(0).recommendedBudget(), actualRecommendations.get(0).recommendedBudget());
        assertEquals(expectedRecommendations.get(1).campaignId(), actualRecommendations.get(1).campaignId());
        assertEquals(expectedRecommendations.get(1).optimisationId(), actualRecommendations.get(1).optimisationId());
        assertEquals(expectedRecommendations.get(1).recommendedBudget(), actualRecommendations.get(1).recommendedBudget());
    }

    @Test
    @DisplayName("Updated raws by count of given recommendations when optimisation status not applied")
    void givenRecommendations_whenApplyLatestRecommendations_thenReturnUpdatedRaws() {
        List<RecommendationDto> recommendations = List.of(this.recommendationDtoOne, this.recommendationDtoTwo);
        Optimisation optimisation = this.optimisationWithStatusApplied;
        when(this.campaignService.updateCampaign(this.recommendationDtoOne.campaignId(), this.recommendationDtoOne.recommendedBudget())).thenReturn(1);
        when(this.campaignService.updateCampaign(this.recommendationDtoTwo.campaignId(), this.recommendationDtoTwo.recommendedBudget())).thenReturn(1);
        when(this.optimisationRepository.save(optimisation)).thenReturn(optimisation);
        doNothing().when(this.recommendationService).storeRecommendations(recommendations);

        int updatedRaws = this.optimisationService.applyRecommendations(recommendations, this.optimisationDtoWithStatusApplied);

        assertEquals(updatedRaws, recommendations.size());

        verify(this.recommendationService, times(1)).storeRecommendations(recommendations);
    }
}
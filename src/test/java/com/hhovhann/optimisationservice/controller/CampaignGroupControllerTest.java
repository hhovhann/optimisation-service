package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.OptimisationStatus;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.repository.CampaignGroupRepository;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import com.hhovhann.optimisationservice.service.OptimisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CampaignGroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignGroupRepository campaignGroupRepository;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private OptimisationRepository optimisationRepository;

    @MockBean
    private OptimisationService optimisationService;

    private CampaignGroupDto campaignGroupDto;

    private CampaignDto campaignDto;

    private OptimisationDto optimisationDto;

    private Recommendation recommendation;

    @BeforeEach
    public void setup() {
        this.campaignGroupDto = new CampaignGroupDto(1L, "Campaign Group One");
        this.campaignDto = new CampaignDto(1L, "Fist Campaign", this.campaignGroupDto.id(), BigDecimal.ONE, 123D, BigDecimal.TEN);
        this.optimisationDto = new OptimisationDto(1L, this.campaignGroupDto.id(),OptimisationStatus.NOT_APPLIED.name());

        this.recommendation = Recommendation.builder()
                .id(1L)
                .campaignId(this.campaignDto.id())
                .optimisationId(this.optimisationDto.id())
                .recommendedBudget(BigDecimal.TEN).build();
    }

    @Test
    @DisplayName("Return not found when no campaign groups are not provided")
    void givenNoCampaignGroups_WhenGetRequest_thenReturnNotFound() throws Exception {
        given(campaignGroupRepository.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/campaign/campaigngroups"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Return campaign group when campaign group are provided")
    void givenCampaignGroups_whenGetCampaignGroups_thenReturnJsonArray() throws Exception {
        given(this.campaignGroupRepository.findAllCampaignGroupDto_Named()).willReturn(Collections.singletonList(this.campaignGroupDto));

        mockMvc.perform(get("/api/v1/campaign/campaigngroups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name", is(this.campaignGroupDto.name())))
                .andExpect(jsonPath("$.[0].id", is(this.campaignGroupDto.id()), Long.class));
    }

    @Test
    void givenCampaignGroupId_whenCampaignsForGroup_thenReturnJsonArray() throws Exception {
        given(this.campaignRepository.findByCampaignGroupId(any())).willReturn(Collections.singletonList(this.campaignDto));

        mockMvc.perform(get("/api/v1/campaign/campaigngroups/{campaignGroupId}/campaigns", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name", is(this.campaignDto.name())))
                .andExpect(jsonPath("$.[0].id", is(this.campaignDto.id()), Long.class))
                .andExpect(jsonPath("$.[0].campaignGroupId", is(this.campaignDto.campaignGroupId()), Long.class))
                .andExpect(jsonPath("$.[0].budget", is(this.campaignDto.budget()), BigDecimal.class))
                .andExpect(jsonPath("$.[0].impressions", is(this.campaignDto.impressions())))
                .andExpect(jsonPath("$.[0].revenue", is(this.campaignDto.revenue()), BigDecimal.class));
    }

    @Test
    void givenCampaignGroupId_whenZeroCampaignsForGroup_thenReturnNotFound() throws Exception {
        given(this.campaignRepository.findByCampaignGroupId(any())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/campaign/campaigngroups/1/campaigns"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenCampaignGroupId_whenOptimisationsForGroup_thenReturnJsonArray() throws Exception {
        given(this.optimisationService.getLatestOptimisationForCampaignGroup(any()))
                .willReturn(java.util.Optional.ofNullable(this.optimisationDto));

        mockMvc.perform(get("/api/v1/campaign/campaigngroups/{campaignGroupId}/optimisations", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(this.optimisationDto.id()), Long.class))
                .andExpect(jsonPath("$.campaignGroupId", is(this.optimisationDto.id()), Long.class))
                .andExpect(jsonPath("$.status", is(this.optimisationDto.status())));
    }

    @Test
    void givenCampaignGroupId_whenZeroOptimisationsForGroup_thenReturnNotFound() throws Exception {
        given(this.optimisationService.getLatestOptimisationForCampaignGroup(any())).willReturn(empty());

        mockMvc.perform(get("/api/v1/campaign/campaigngroups/{campaignGroupId}/optimisations", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenOptimisationId_whenRecommendationsForOptimisation_thenReturnJsonArray() throws Exception {
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.singletonList(this.recommendation));

        mockMvc.perform(get("/api/v1/campaign/optimisations/{optimisationId}/recommendations", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(this.recommendation.getId()), Long.class))
                .andExpect(jsonPath("$.[0].campaignId", is(this.recommendation.getCampaignId()), Long.class))
                .andExpect(jsonPath("$.[0].optimisationId", is(this.recommendation.getOptimisationId()), Long.class))
                .andExpect(jsonPath("$.[0].recommendedBudget", is(this.recommendation.getRecommendedBudget()), BigDecimal.class));

    }

    @Test
    void givenOptimisationId_whenZeroRecommendationsForOptimisation_thenReturnNotFound() throws Exception {
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/campaign/optimisations/{optimisationId}/recommendations", 1))
                .andExpect(status().isNotFound());
    }


    @Test
    void givenOptimisationId_whenApplyRecommendation_thenCampaignBudgetUpdated() throws Exception {
        given(this.optimisationRepository.findOptimisationDtoById_Named(any())).willReturn(of(this.optimisationDto));
        given(this.optimisationService.getOptimisation(any())).willReturn(of(this.optimisationDto));
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.singletonList(this.recommendation));
        given(this.optimisationService.applyRecommendations(any(), any())).willReturn(1);

        mockMvc.perform(post("/api/v1/campaign/optimisations/{optimisationId}/recommendations", this.optimisationDto.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Updated Campaigns 1")));
    }
}
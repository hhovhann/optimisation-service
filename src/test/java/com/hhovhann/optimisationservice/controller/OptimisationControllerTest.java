package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.OptimisationStatus;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import com.hhovhann.optimisationservice.service.OptimisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

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
class OptimisationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptimisationRepository optimisationRepository;

    @MockBean
    private OptimisationService optimisationService;

    private CampaignGroupDto campaignGroupDto;

    private CampaignDto campaignDto;

    private OptimisationDto optimisationDto;

    private RecommendationDto recommendationDto;

    @BeforeEach
    public void setup() {
        this.campaignGroupDto = new CampaignGroupDto(1L, "Campaign Group One");
        this.campaignDto = new CampaignDto(1L, "Fist Campaign", this.campaignGroupDto.id(), BigDecimal.ONE, 123D, BigDecimal.TEN);
        this.optimisationDto = new OptimisationDto(1L, this.campaignGroupDto.id(), OptimisationStatus.NOT_APPLIED.name());
        this.recommendationDto = new RecommendationDto(1L, this.campaignDto.id(), this.optimisationDto.id(), BigDecimal.TEN);
    }


    @Test
    void givenOptimisationId_whenRecommendationsForOptimisation_thenReturnJsonArray() throws Exception {
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.singletonList(this.recommendationDto));

        mockMvc.perform(get("/api/v1/optimisations/{optimisationId}/recommendations", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(this.recommendationDto.id()), Long.class))
                .andExpect(jsonPath("$.[0].campaignId", is(this.recommendationDto.campaignId()), Long.class))
                .andExpect(jsonPath("$.[0].optimisationId", is(this.recommendationDto.optimisationId()), Long.class))
                .andExpect(jsonPath("$.[0].recommendedBudget", is(this.recommendationDto.recommendedBudget()), BigDecimal.class));

    }

    @Test
    void givenOptimisationId_whenZeroRecommendationsForOptimisation_thenReturnNotFound() throws Exception {
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/optimisations/{optimisationId}/recommendations", 1))
                .andExpect(status().isNotFound());
    }


    @Test
    void givenOptimisationId_whenApplyRecommendation_thenCampaignBudgetUpdated() throws Exception {
        given(this.optimisationRepository.findOptimisationDtoById_Named(any())).willReturn(of(this.optimisationDto));
        given(this.optimisationService.getOptimisation(any())).willReturn(of(this.optimisationDto));
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.singletonList(this.recommendationDto));
        given(this.optimisationService.applyRecommendations(any(), any())).willReturn(1);

        mockMvc.perform(post("/api/v1/optimisations/{optimisationId}/recommendations", this.optimisationDto.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Updated Campaigns 1")));
    }
}
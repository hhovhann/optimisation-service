package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.entity.*;
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

    private CampaignGroup campaignGroup;

    private Campaign campaign;

    private Optimisation optimisation;

    private Recommendation recommendation;

    @BeforeEach
    public void setup() {

        this.campaignGroup = CampaignGroup.builder()
                .id(1L)
                .name("Campaign Group One").build();

        this.campaign = Campaign.builder()
                .id(1L)
                .campaignGroupId(this.campaignGroup.getId())
                .budget(BigDecimal.ONE)
                .impressions(123D)
                .name("Fist Campaign")
                .revenue(BigDecimal.TEN)
                .build();

        this.optimisation = Optimisation.builder()
                .id(1L)
                .campaignGroupId(this.campaignGroup.getId())
                .status(OptimisationStatus.NOT_APPLIED)
                .build();

        this.recommendation = Recommendation.builder()
                .id(1L)
                .campaignId(this.campaign.getId())
                .optimisationId(this.optimisation.getId())
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
        given(this.campaignGroupRepository.findAll()).willReturn(Collections.singletonList(this.campaignGroup));

        mockMvc.perform(get("/api/v1/campaign/campaigngroups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name", is(this.campaignGroup.getName())))
                .andExpect(jsonPath("$.[0].id", is(this.campaignGroup.getId()), Long.class));
    }

    @Test
    void givenCampaignGroupId_whenCampaignsForGroup_thenReturnJsonArray() throws Exception {
        given(this.campaignRepository.findByCampaignGroupId(any())).willReturn(Collections.singletonList(this.campaign));

        mockMvc.perform(get("/api/v1/campaign/campaigngroups/{campaignGroupId}/campaigns", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name", is(this.campaign.getName())))
                .andExpect(jsonPath("$.[0].id", is(this.campaign.getId()), Long.class))
                .andExpect(jsonPath("$.[0].campaignGroupId", is(this.campaign.getCampaignGroupId()), Long.class))
                .andExpect(jsonPath("$.[0].budget", is(this.campaign.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.[0].impressions", is(this.campaign.getImpressions())))
                .andExpect(jsonPath("$.[0].revenue", is(this.campaign.getRevenue()), BigDecimal.class));
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
                .willReturn(java.util.Optional.ofNullable(this.optimisation));

        mockMvc.perform(get("/api/v1/campaign/campaigngroups/{campaignGroupId}/optimisations", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(this.optimisation.getId()), Long.class))
                .andExpect(jsonPath("$.campaignGroupId", is(this.optimisation.getCampaignGroupId()), Long.class))
                .andExpect(jsonPath("$.status", is(this.optimisation.getStatus().name())));
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
    void givenOptimisationId_whenZeroRecommendationsForOptimisation_thenReturnNotFound() throws Exception{
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/campaign/optimisations/{optimisationId}/recommendations", 1))
                .andExpect(status().isNotFound());
    }


    @Test
    void givenOptimisationId_whenApplyRecommendation_thenCampaignBudgetUpdated() throws Exception{
        given(this.optimisationRepository.findById(any())).willReturn(of(this.optimisation));
        given(this.optimisationService.getOptimisation(any())).willReturn(of(this.optimisation));
        given(this.optimisationService.getLatestRecommendations(any())).willReturn(Collections.singletonList(this.recommendation));
        given(this.optimisationService.applyRecommendations(any(), any())).willReturn(1);
        this.campaign.setBudget(this.recommendation.getRecommendedBudget());

        mockMvc.perform(post("/api/v1/campaign/optimisations/{optimisationId}/recommendations", this.optimisation.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Updated Campaigns 1")));
    }
}
package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import com.hhovhann.optimisationservice.service.CampaignGroupService;
import com.hhovhann.optimisationservice.service.CampaignService;
import com.hhovhann.optimisationservice.service.OptimisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hhovhann.optimisationservice.model.entity.OptimisationStatus.APPLIED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/campaign/")
public class CampaignGroupController {

    private final CampaignService campaignService;
    private final CampaignGroupService campaignGroupService;
    private final OptimisationService optimisationService;

    @Autowired
    public CampaignGroupController(CampaignService campaignService,
                                   CampaignGroupService campaignGroupService,
                                   OptimisationService optimisationService) {
        this.campaignService = campaignService;
        this.campaignGroupService = campaignGroupService;
        this.optimisationService = optimisationService;
    }


    @ResponseBody
    @GetMapping(value = "/campaigngroups", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CampaignGroup>> retrieveAllCampaignGroups() {
        List<CampaignGroup> campaignGroups = campaignGroupService.findAllCampaignGroups();

        return CollectionUtils.isEmpty(campaignGroups) ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(campaignGroups);
    }

    @ResponseBody
    @GetMapping(value = "/campaigngroups/{campaignGroupId}/campaigns", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Campaign>> retrieveAllCampaignsForCampaignGroup(@PathVariable Long campaignGroupId) {
        List<Campaign> campaigns = campaignService.getCampaignsForCampaignGroup(campaignGroupId);

        return campaigns.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(campaigns);
    }

    @ResponseBody
    @GetMapping(value = "/campaigngroups/{campaignGroupId}/optimisations", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Optimisation> retrieveLatestOptimisationForCampaignGroup(@PathVariable Long campaignGroupId) {
        Optional<Optimisation> optimisation = this.optimisationService.getLatestOptimisationForCampaignGroup(campaignGroupId);

        return optimisation.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(optimisation.get());
    }

    @ResponseBody
    @GetMapping(value = "/optimisations/{optimisationId}/recommendations", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Recommendation>> retrieveLatestRecommendationsForOptimisation(@PathVariable Long optimisationId) {
        List<Recommendation> recommendations = this.optimisationService.getLatestRecommendations(optimisationId);

        return recommendations.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(recommendations);
    }

    @ResponseBody
    @PostMapping(value = "/optimisations/{optimisationId}/recommendations", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> applyLatestRecommendation(@PathVariable Long optimisationId) {
        Optional<Optimisation> optimisation = optimisationService.getOptimisation(optimisationId);

        if (optimisation.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (optimisation.get().getStatus().equals(APPLIED)) {
            return ResponseEntity.ok().build();
        }

        List<Recommendation> recommendations = this.optimisationService.getLatestRecommendations(optimisationId);
        var rowsUpdated = this.optimisationService.applyRecommendations(recommendations, optimisation.get());

        return ResponseEntity.ok().body(Map.of("message", "Updated Campaigns " + rowsUpdated));
    }
}

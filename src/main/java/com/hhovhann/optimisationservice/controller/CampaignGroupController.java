package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.service.CampaignGroupService;
import com.hhovhann.optimisationservice.service.CampaignService;
import com.hhovhann.optimisationservice.service.OptimisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/")
public class CampaignGroupController {

    private final CampaignService campaignService;
    private final CampaignGroupService campaignGroupService;
    private final OptimisationService optimisationService;

    public CampaignGroupController(CampaignService campaignService,
                                   CampaignGroupService campaignGroupService,
                                   OptimisationService optimisationService) {
        this.campaignService = campaignService;
        this.campaignGroupService = campaignGroupService;
        this.optimisationService = optimisationService;
    }


    @ResponseBody
    @GetMapping(value = "/campaigngroups", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CampaignGroupDto>> retrieveAllCampaignGroups() {
        return ResponseEntity.ok().body(campaignGroupService.findAllCampaignGroups());
    }

    @ResponseBody
    @GetMapping(value = "/campaigngroups/{campaignGroupId}/campaigns", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CampaignDto>> retrieveAllCampaignsForCampaignGroup(@PathVariable Long campaignGroupId) {
        return ResponseEntity.ok().body(campaignService.getCampaignsForCampaignGroup(campaignGroupId));
    }

    @ResponseBody
    @GetMapping(value = "/campaigngroups/{campaignGroupId}/optimisations", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<OptimisationDto> retrieveLatestOptimisationForCampaignGroup(@PathVariable Long campaignGroupId) {
        return ResponseEntity.ok().body(this.optimisationService.getLatestOptimisationForCampaignGroup(campaignGroupId));
    }
}

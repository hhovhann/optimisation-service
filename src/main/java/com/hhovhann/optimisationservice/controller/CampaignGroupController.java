package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.entity.*;
import com.hhovhann.optimisationservice.repository.CampaignGroupRepository;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import com.hhovhann.optimisationservice.repository.OptimisationRepository;
import com.hhovhann.optimisationservice.service.OptimisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "campaign/api/v1")
public class CampaignGroupController {

    private final CampaignGroupRepository campaignGroupRepository;
    private final CampaignRepository campaignRepository;
    private final OptimisationRepository optimisationRepository;
    private final OptimisationService optimisationService;

    public static final String HEADER_NAME = "Content-Type";
    public static final String HEADER_VALUE = "application/json; charset=utf-8";

    @Autowired
    public CampaignGroupController(CampaignGroupRepository campaignGroupRepository,
                                   CampaignRepository campaignRepository,
                                   OptimisationRepository optimisationRepository,
                                   OptimisationService optimisationService) {
        this.campaignGroupRepository = campaignGroupRepository;
        this.campaignRepository = campaignRepository;
        this.optimisationRepository = optimisationRepository;
        this.optimisationService = optimisationService;
    }


    @GetMapping(value = "/campaigngroups/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CampaignGroup>> getCampaignGroups() {
        List<CampaignGroup> campaignGroups = this.campaignGroupRepository.findAll();
        var headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_VALUE);
        return CollectionUtils.isEmpty(campaignGroups) ? ResponseEntity.notFound().headers(headers).build()
                : ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).headers(headers).body(campaignGroups);
    }


    @GetMapping(value = "/campaigngroups/{campaignGroupId}/campaigns/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Campaign>> getCampaignsForGroup(@PathVariable Long campaignGroupId) {
        List<Campaign> campaigns = this.campaignRepository.findByCampaignGroupId(campaignGroupId);
        var headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_VALUE);
        return campaigns.isEmpty() ? ResponseEntity.notFound().headers(headers).build()
                : ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).headers(headers).body(campaigns);
    }

    @GetMapping(value = "/campaigngroups/{campaignGroupId}/optimisations/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optimisation> getOptimisationForGroup(@PathVariable Long campaignGroupId) {
        Optional<Optimisation> optimisation = this.optimisationService.getLatestOptimisation(campaignGroupId);
        var headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_VALUE);
        return optimisation.isEmpty() ? ResponseEntity.notFound().headers(headers).build()
                : ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).headers(headers).body(optimisation.get());
    }


    @GetMapping(value = "/optimisations/{optimisationId}/recommendations/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Recommendation>> getRecommendationsForOptimisation(@PathVariable Long optimisationId) {
        List<Recommendation> recommendations = this.optimisationService.getLatestRecommendations(optimisationId);
        var headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_VALUE);
        return recommendations.isEmpty() ? ResponseEntity.notFound().headers(headers).build()
                : ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).headers(headers).body(recommendations);
    }


    @PostMapping(value = "/optimisations/{optimisationId}/recommendations/apply", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Map<String, String>> applyLatestRecommendation(@PathVariable Long optimisationId) {

        var rowsUpdated = optimisationService.applyLatestRecommendation(optimisationId);
//        Optional<Optimisation> optimisation = this.optimisationRepository.findById(optimisationId);
//

//
//        if (optimisation.isEmpty()) {
//            return ResponseEntity.notFound().headers(headers).build();
//        }
//
//        if (optimisation.get().getStatus().equals(OptimisationStatus.APPLIED)) {
//            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).headers(headers).build();
//        }
//
//        List<Recommendation> recommendations = this.optimisationService.getLatestRecommendations(optimisationId);
//        var rowsUpdated = this.optimisationService.applyRecommendations(recommendations, optimisation.get());

        var headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_VALUE);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .headers(headers)
                .body(Map.of("message", "Campaigns Updated " + rowsUpdated));
    }
}

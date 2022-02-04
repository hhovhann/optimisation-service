package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.dto.RecommendationDto;
import com.hhovhann.optimisationservice.service.OptimisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.hhovhann.optimisationservice.model.OptimisationStatus.APPLIED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/")
public class OptimisationController {

    private final OptimisationService optimisationService;

    @Autowired
    public OptimisationController(OptimisationService optimisationService) {
        this.optimisationService = optimisationService;
    }

    @ResponseBody
    @GetMapping(value = "/optimisations/{optimisationId}/recommendations", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecommendationDto>> retrieveLatestRecommendationsForOptimisation(@PathVariable Long optimisationId) {
        return ResponseEntity.ok().body(this.optimisationService.getLatestRecommendations(optimisationId));
    }

    @ResponseBody
    @PostMapping(value = "/optimisations/{optimisationId}/recommendations", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> applyLatestRecommendation(@PathVariable Long optimisationId) {
        OptimisationDto optimisation = optimisationService.getOptimisation(optimisationId);
        if (Objects.equals(APPLIED.name(), optimisation.status())) {
            return ResponseEntity.ok().build();
        }

        List<RecommendationDto> recommendations = this.optimisationService.getLatestRecommendations(optimisationId);
        var updatedCampaignsCount = this.optimisationService.applyRecommendations(recommendations, optimisation);

        return ResponseEntity.ok().body(Map.of("message", "Updated Campaigns " + updatedCampaignsCount));
    }
}

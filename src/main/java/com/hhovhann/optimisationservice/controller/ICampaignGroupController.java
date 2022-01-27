package com.hhovhann.optimisationservice.controller;

import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import com.hhovhann.optimisationservice.model.entity.Recommendation;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

public interface ICampaignGroupController {
    ResponseEntity<List<CampaignGroup>> getCampaignGroups();

    ResponseEntity<List<Campaign>> getCampaignsForGroup(Long campaignGroupId);

    ResponseEntity<Optimisation> getOptimisationForGroup(Long campaignGroupId);

    ResponseEntity<List<Recommendation>> getRecommendationsForOptimisation(Long optimisationId);

    ResponseEntity<Map<String, String>> applyLatestRecommendation(Long campaignGroupId);
}

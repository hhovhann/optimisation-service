package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.entity.Campaign;

import java.math.BigDecimal;
import java.util.List;

public interface CampaignService {
    List<Campaign> getCampaignsForGroup(Long campaignGroupId);

    int updateCampaign(Long campaignId, BigDecimal recommendedBudget);
}

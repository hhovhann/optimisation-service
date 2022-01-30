package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.CampaignDto;

import java.math.BigDecimal;
import java.util.List;

public interface CampaignService {
    List<CampaignDto> getCampaignsForCampaignGroup(Long campaignGroupId);

    int updateCampaign(Long campaignId, BigDecimal recommendedBudget);
}

package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;

import java.util.List;

public interface CampaignGroupService {
    List<CampaignGroupDto> findAllCampaignGroups();
}

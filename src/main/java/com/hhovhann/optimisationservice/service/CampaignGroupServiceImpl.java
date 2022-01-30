package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.repository.CampaignGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignGroupServiceImpl implements CampaignGroupService {
    private final CampaignGroupRepository campaignGroupRepository;

    public CampaignGroupServiceImpl(CampaignGroupRepository campaignGroupRepository) {
        this.campaignGroupRepository = campaignGroupRepository;
    }

    @Override
    public List<CampaignGroupDto> findAllCampaignGroups() {
        return campaignGroupRepository.findAllCampaignGroupDto_Named();
    }
}

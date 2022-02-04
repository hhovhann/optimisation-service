package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.exception.CampaignGroupNotFoundException;
import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.repository.CampaignGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CampaignGroupServiceImpl implements CampaignGroupService {
    private final CampaignGroupRepository campaignGroupRepository;

    public CampaignGroupServiceImpl(CampaignGroupRepository campaignGroupRepository) {
        this.campaignGroupRepository = campaignGroupRepository;
    }

    @Override
    public List<CampaignGroupDto> findAllCampaignGroups() {
        List<CampaignGroupDto> campaignGroupDtos = campaignGroupRepository.findAllCampaignGroupDto_Named();
        if (campaignGroupDtos.isEmpty()) {
            throw new CampaignGroupNotFoundException("No campaign group found by provided id");
        }
        return campaignGroupDtos;
    }
}

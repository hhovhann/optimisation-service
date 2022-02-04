package com.hhovhann.optimisationservice.service;

import com.hhovhann.optimisationservice.exception.CampaignGroupNotFoundException;
import com.hhovhann.optimisationservice.mapper.CampaignGroupMapper;
import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import com.hhovhann.optimisationservice.repository.CampaignGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignGroupServiceImpl implements CampaignGroupService {
    private final CampaignGroupRepository campaignGroupRepository;
    private final CampaignGroupMapper campaignGroupMapper;

    public CampaignGroupServiceImpl(CampaignGroupRepository campaignGroupRepository, CampaignGroupMapper campaignGroupMapper) {
        this.campaignGroupRepository = campaignGroupRepository;
        this.campaignGroupMapper = campaignGroupMapper;
    }

    @Override
    public List<CampaignGroupDto> findAllCampaignGroups() {
        List<CampaignGroup> campaignGroups = campaignGroupRepository.findAll();
        if (campaignGroups.isEmpty()) {
            throw new CampaignGroupNotFoundException("No campaign group found by provided id");
        }
        return campaignGroupMapper.toDto(campaignGroups);
    }
}

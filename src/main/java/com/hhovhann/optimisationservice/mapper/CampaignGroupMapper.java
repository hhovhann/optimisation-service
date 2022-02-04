package com.hhovhann.optimisationservice.mapper;

import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import com.hhovhann.optimisationservice.model.entity.CampaignGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CampaignGroupMapper {
    public List<CampaignGroupDto> toDto(List<CampaignGroup> campaignGroups) {
        return campaignGroups
                .stream()
                .map(campaignGroup -> new CampaignGroupDto(campaignGroup.getId(), campaignGroup.getName()))
                .collect(Collectors.toList());
    }
}

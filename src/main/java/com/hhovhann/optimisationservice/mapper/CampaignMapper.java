package com.hhovhann.optimisationservice.mapper;

import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.entity.Campaign;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CampaignMapper {
    public List<CampaignDto> toDto(List<Campaign> campaignGroups) {
        return campaignGroups
                .stream()
                .map(campaign -> new CampaignDto(campaign.getId(), campaign.getName(), campaign.getCampaignGroupId(), campaign.getBudget(), campaign.getImpressions(), campaign.getRevenue()))
                .collect(Collectors.toList());
    }
}

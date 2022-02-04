package com.hhovhann.optimisationservice.service;

import java.util.List;
import java.math.BigDecimal;

import com.hhovhann.optimisationservice.exception.CampaignNotFoundException;
import com.hhovhann.optimisationservice.mapper.CampaignMapper;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import org.springframework.stereotype.Service;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    public CampaignServiceImpl(CampaignRepository campaignRepository, CampaignMapper campaignMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
    }

    @Override
    public List<CampaignDto> getCampaignsForCampaignGroup(Long campaignGroupId) {
        List<Campaign> campaigns = campaignRepository.findByCampaignGroupId(campaignGroupId);
        if (campaigns.isEmpty()) {
            throw new CampaignNotFoundException("No campaign found by provided id");
        }
        return campaignMapper.toDto(campaigns);
    }

    @Override
    public int updateCampaign(Long campaignId, BigDecimal recommendedBudget) {
        return this.campaignRepository.updateCampaign(campaignId, recommendedBudget);
    }
}

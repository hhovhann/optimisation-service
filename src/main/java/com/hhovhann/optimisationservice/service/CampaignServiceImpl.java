package com.hhovhann.optimisationservice.service;

import java.util.List;
import java.math.BigDecimal;

import com.hhovhann.optimisationservice.exception.CampaignNotFoundException;
import com.hhovhann.optimisationservice.model.dto.CampaignDto;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public List<CampaignDto> getCampaignsForCampaignGroup(Long campaignGroupId) {
        List<CampaignDto> campaignDtos = campaignRepository.findByCampaignGroupId(campaignGroupId);
        if (campaignDtos.isEmpty()) {
            throw new CampaignNotFoundException("No campaign found by provided id");
        }
        return campaignDtos;
    }

    @Override
    public int updateCampaign(Long campaignId, BigDecimal recommendedBudget) {
        return this.campaignRepository.updateCampaign(campaignId, recommendedBudget);
    }
}

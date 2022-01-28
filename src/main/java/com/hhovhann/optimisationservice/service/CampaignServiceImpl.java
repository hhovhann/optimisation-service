package com.hhovhann.optimisationservice.service;

import java.util.List;
import java.math.BigDecimal;
import com.hhovhann.optimisationservice.model.entity.Campaign;
import com.hhovhann.optimisationservice.repository.CampaignRepository;
import org.springframework.stereotype.Service;

@Service
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public List<Campaign> getCampaignsForCampaignGroup(Long campaignGroupId) {
        return campaignRepository.findByCampaignGroupId(campaignGroupId);
    }

    @Override
    public int updateCampaign(Long campaignId, BigDecimal recommendedBudget) {
        return this.campaignRepository.updateCampaign(campaignId, recommendedBudget);
    }
}

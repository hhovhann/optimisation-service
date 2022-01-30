package com.hhovhann.optimisationservice.model.dto;

import java.math.BigDecimal;

public record CampaignDto(Long id, String name, Long campaignGroupId, BigDecimal budget, Double impressions, BigDecimal revenue) {
}

package com.hhovhann.optimisationservice.model.dto;

import java.math.BigDecimal;

record RecommendationDto(Long id, Long campaignId, Long optimisationId, BigDecimal recommendedBudget) {
}

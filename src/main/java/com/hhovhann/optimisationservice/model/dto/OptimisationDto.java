package com.hhovhann.optimisationservice.model.dto;

import com.hhovhann.optimisationservice.model.OptimisationStatus;

public record OptimisationDto(Long id, Long campaignGroupId, OptimisationStatus status) {
}

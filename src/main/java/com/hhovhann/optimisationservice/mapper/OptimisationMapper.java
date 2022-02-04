package com.hhovhann.optimisationservice.mapper;

import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OptimisationMapper {

    public List<OptimisationDto> toDto(List<Optimisation> optimisations) {
        return optimisations
                .stream()
                .map(optimisation -> new OptimisationDto(optimisation.getId(), optimisation.getCampaignGroupId(), optimisation.getStatus().name()))
                .collect(Collectors.toList());
    }
}

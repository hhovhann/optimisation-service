package com.hhovhann.optimisationservice.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DBRider
@SpringBootTest
@DBUnit(cacheConnection = false, leakHunter = true)
class OptimisationRepositoryTest {

    @Autowired
    private OptimisationRepository optimisationRepository;

    @Test
    @DataSet("datasets/optimisation/optimisations.yml")
    void shouldReturnListOfOptimisationDto_WhenOptimisationIdProvided() {
        List<OptimisationDto>optimisationDtos = optimisationRepository.findOptimisationDtoByCampaignGroupIdOrderByIdDesc_Named(1L);

        assertEquals(optimisationDtos.get(0).id(), 1);
        assertEquals(optimisationDtos.get(0).campaignGroupId(), 1);
        assertEquals(optimisationDtos.get(0).status(), "NOT_APPLIED");

        assertEquals(optimisationDtos.get(1).id(), 2);
        assertEquals(optimisationDtos.get(1).campaignGroupId(), 1);
        assertEquals(optimisationDtos.get(1).status(), "APPLIED");
    }

    @Test
    @DataSet("datasets/optimisation/optimisation.yml")
    void shouldReturnOptimisationDto_WhenOptimisationIdProvided() {
        Optional<OptimisationDto> optimisationDtoOptional = optimisationRepository.findOptimisationDtoById_Named(1L);
        OptimisationDto optimisationDto = optimisationDtoOptional.get();

        assertEquals(optimisationDto.id(), 1);
        assertEquals(optimisationDto.campaignGroupId(), 1);
        assertEquals(optimisationDto.status(), "NOT_APPLIED");
    }
}
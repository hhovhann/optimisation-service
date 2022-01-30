package com.hhovhann.optimisationservice.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.hhovhann.optimisationservice.model.OptimisationStatus.NOT_APPLIED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DBRider
@SpringBootTest
@DBUnit(cacheConnection = false, leakHunter = true)
class OptimisationRepositoryTest {

    @Autowired
    private OptimisationRepository optimisationRepository;

    @Test
    @DataSet("optimisation/optimisation.yml")
    void shouldReturnListOfOptimisations_whenCampaignGroupIdProvided() {
        List<Optimisation> optimisations = optimisationRepository.findByCampaignGroupIdOrderByIdDesc(1L);

        assertEquals(optimisations.size(), 1);
        assertEquals(optimisations.get(0).getCampaignGroupId(), 1);
        assertEquals(optimisations.get(0).getStatus(), NOT_APPLIED);
    }

    @Test
    @DataSet("optimisation/optimisation.yml")
    void shouldReturnOptimisationDto_WhenOptimisationIdProvided() {
        Optional<OptimisationDto> optimisationDtoOptional = optimisationRepository.findOptimisationDtoById_Named(1L);
        OptimisationDto optimisationDto = optimisationDtoOptional.get();

        assertEquals(optimisationDto.id(), 1);
        assertEquals(optimisationDto.campaignGroupId(), 1);
        assertEquals(optimisationDto.status(), "NOT_APPLIED");
    }
}
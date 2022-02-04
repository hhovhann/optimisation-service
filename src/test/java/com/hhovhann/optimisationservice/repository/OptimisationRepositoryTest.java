package com.hhovhann.optimisationservice.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.hhovhann.optimisationservice.model.entity.Optimisation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.hhovhann.optimisationservice.model.OptimisationStatus.APPLIED;
import static com.hhovhann.optimisationservice.model.OptimisationStatus.NOT_APPLIED;
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
        List<Optimisation>optimisationDtos = optimisationRepository.findByCampaignGroupIdOrderByIdDesc(1L);

        assertEquals(optimisationDtos.get(0).getId(), 2);
        assertEquals(optimisationDtos.get(0).getCampaignGroupId(), 1);
        assertEquals(optimisationDtos.get(0).getStatus(), APPLIED);

        assertEquals(optimisationDtos.get(1).getId(), 1);
        assertEquals(optimisationDtos.get(1).getCampaignGroupId(), 1);
        assertEquals(optimisationDtos.get(1).getStatus(), NOT_APPLIED);
    }

    @Test
    @DataSet("datasets/optimisation/optimisation.yml")
    void shouldReturnOptimisationDto_WhenOptimisationIdProvided() {
        Optional<Optimisation> optimisationDtoOptional = optimisationRepository.findById(1L);
        Optimisation optimisationDto = optimisationDtoOptional.get();

        assertEquals(optimisationDto.getId(), 1);
        assertEquals(optimisationDto.getCampaignGroupId(), 1);
        assertEquals(optimisationDto.getStatus(), NOT_APPLIED);
    }
}
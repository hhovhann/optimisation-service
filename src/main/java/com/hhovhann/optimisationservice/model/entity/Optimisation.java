package com.hhovhann.optimisationservice.model.entity;

import com.hhovhann.optimisationservice.model.OptimisationStatus;
import com.hhovhann.optimisationservice.model.dto.OptimisationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;


@NamedNativeQuery(
        name = "Optimisation.findOptimisationDtoById_Named",
        query = "SELECT optimisation.ID as id, optimisation.CAMPAIGN_GROUP_ID as campaignGroupId, optimisation.STATUS as status FROM Optimisation optimisation WHERE id = :id",
        resultSetMapping = "Mapping.OptimisationDto")
@SqlResultSetMapping(
        name = "Mapping.OptimisationDto",
        classes = @ConstructorResult(
                targetClass = OptimisationDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "campaignGroupId", type = Long.class),
                        @ColumnResult(name = "status", type = String.class)
                }))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OPTIMISATION")
public class Optimisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Long id;

    @JoinColumn(name = "CAMPAIGN_GROUP_ID")
    Long campaignGroupId;

    @NonNull
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    OptimisationStatus status;
}

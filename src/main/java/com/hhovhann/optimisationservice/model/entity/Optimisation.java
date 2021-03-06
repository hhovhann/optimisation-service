package com.hhovhann.optimisationservice.model.entity;

import com.hhovhann.optimisationservice.model.OptimisationStatus;
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

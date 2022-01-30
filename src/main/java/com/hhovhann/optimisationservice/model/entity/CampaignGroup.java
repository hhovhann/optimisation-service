package com.hhovhann.optimisationservice.model.entity;

import com.hhovhann.optimisationservice.model.dto.CampaignGroupDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@NamedNativeQuery(
        name = "CampaignGroup.findAllCampaignGroupDto_Named",
        query = "SELECT campaignGroup.id as id, campaignGroup.name as name FROM CAMPAIGN_GROUP campaignGroup",
        resultSetMapping = "Mapping.CampaignGroupDto")
@SqlResultSetMapping(
        name = "Mapping.CampaignGroupDto",
        classes = @ConstructorResult(
                targetClass = CampaignGroupDto.class,
                columns = {@ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "name", type = String.class)}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CAMPAIGN_GROUP")
public class CampaignGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Long id;

    @NonNull
    @Column(name = "NAME", length = 200)
    String name;
}

package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.DepartmentEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "unqualified")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class UnqualifiedEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inspection_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("inspectionId")
    @NonNull
    private InspectionEntity inspectionEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("creatorId")
    @NonNull
    private UserEntity creator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "responsible_department_id", referencedColumnName = "id")
    @JsonProperty("departmentId")
    private DepartmentEntity department;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "responsible_person_id", referencedColumnName = "id")
    @JsonProperty("responsiblePersonId")
    private UserEntity responsiblePerson;

    @Column(name = "unqualified_level", columnDefinition = "VARCHAR(32)")
    private String unqualifiedLevel;

    @Column(name = "rework_quantity", columnDefinition = "INTEGER")
    private Integer reworkQuantity;

    @Column(name = "rework_details", columnDefinition = "VARCHAR(256)")
    private String reworkDetails;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    @JsonProperty("operatorId")
    private UserEntity operator;

    @Column(name = "rework_inspection", columnDefinition = "VARCHAR(512)")
    private String reworkInspection;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rework_inspector_id", referencedColumnName = "id")
    @JsonProperty("reworkInspectorId")
    private UserEntity reworkInspector;

    @Column(name = "no_rework_quantity", columnDefinition = "INTEGER")
    private Integer noReworkQuantity;

    @Column(name = "rework_accepted_quantity", columnDefinition = "INTEGER")
    private Integer reworkAcceptedQuantity;

    @Column(name = "no_rework_accepted_quantity", columnDefinition = "INTEGER")
    private Integer noReworkAcceptedQuantity;

    @Column(name = "rejected_quantity", columnDefinition = "INTEGER")
    private Integer rejectedQuantity;

    @Column(name = "influence", columnDefinition = "VARCHAR(256)")
    private String influence;

    @Column(name = "acceptance_details", columnDefinition = "VARCHAR(256)")
    private String acceptanceDetails;

    @Column(name = "standard_of_acceptance", columnDefinition = "VARCHAR(256)")
    private String standardOfAcceptance;

//    @Column(name = "status", columnDefinition = "VARCHAR(32)", nullable = false)
//    @NonNull
//    private InspectionStatus status;

    @Column(name = "created_date", columnDefinition = "DATETIME", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull
    private LocalDateTime createdDate;

//    @JsonProperty("inspectionActions")
//    @OneToMany(orphanRemoval = true, mappedBy = "requisitions")
//    @JsonManagedReference
//    private List<InspectionActionEntity> actions;
}

package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.DepartmentEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "inspection")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class InspectionEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("creatorId")
    @NonNull
    private UserEntity creator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @JsonProperty("departmentId")
    @JsonBackReference
    private DepartmentEntity department;

    @Column(name = "type", columnDefinition = "TINYINT")
    @NonNull
    private InspectionType type;

    @Column(name = "product_name", columnDefinition = "VARCHAR(256)")
    private String productName;

    @Column(name = "model", columnDefinition = "VARCHAR(256)")
    private String model;

    @Column(name = "serial_number", columnDefinition = "VARCHAR(256)")
    private String serialNumber;

    @Column(name = "quantity", columnDefinition = "INTEGER")
    private Long quantity;

    @Column(name = "unit", columnDefinition = "VARCHAR(8)")
    private String unit;

    @Column(name = "manufacturer", columnDefinition = "VARCHAR(256)")
    private String manufacturer;

    @Column(name = "size_fit", columnDefinition = "TINYINT")
    private Byte sizeFit;

    @Column(name = "quality_certificate", columnDefinition = "TINYINT")
    private Byte qualityCertificate;

    @Column(name = "exterior", columnDefinition = "TINYINT")
    private Byte exterior;

    @Column(name = "logo", columnDefinition = "TINYINT")
    private Byte logo;

    @Column(name = "packaging", columnDefinition = "TINYINT")
    private Byte packaging;

    @Column(name = "note", columnDefinition = "VARCHAR(512)")
    private String note;

    @Column(name = "sampling_method", columnDefinition = "VARCHAR(512)")
    private String samplingMethod;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inspector_id", referencedColumnName = "id")
    @JsonProperty("inspectorId")
    private UserEntity inspector;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requisitions_id", referencedColumnName = "id", nullable = false)
    @JsonProperty("requisitionsId")
    @JsonBackReference
    @NonNull
    private RequisitionsEntity requisitions;

    @Column(name = "status", columnDefinition = "VARCHAR(32)", nullable = false)
    @NonNull
    private InspectionStatus status;

    @Column(name = "created_date", columnDefinition = "DATETIME", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull
    private LocalDateTime createdDate;

    @JsonProperty("inspectionActions")
    @OneToMany(orphanRemoval = true, mappedBy = "inspection")
    @JsonManagedReference
    private List<InspectionActionEntity> actions;
}

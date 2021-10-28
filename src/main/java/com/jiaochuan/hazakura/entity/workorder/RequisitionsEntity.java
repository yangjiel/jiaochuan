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

@Table(name = "requisitions")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class RequisitionsEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity creator;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchaser_id", referencedColumnName = "id")
    private UserEntity purchaser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_order_id", referencedColumnName = "id", nullable = false)
    @NonNull
    @JsonBackReference
    private WorkOrderEntity workOrder;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @JsonBackReference
    private DepartmentEntity department;

    @Column(name = "purchase_order_id", columnDefinition = "INTEGER")
    private Long purchaseOrderId;

    @Column(name = "supplier", columnDefinition = "VARCHAR(128)")
    private String supplier;

    @Column(name = "status", columnDefinition = "VARCHAR(32)", nullable = false)
    @NonNull
    private RequisitionsStatus status;

    @Column(name = "created_date", columnDefinition = "DATETIME", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull
    private LocalDateTime createdDate;

    @Column(name = "purchase_date", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    @OneToMany(orphanRemoval = true, mappedBy = "requisitions")
    @JsonManagedReference
    private List<RequisitionsEquipmentEntity> equipments;

    @JsonProperty("requisitionsActions")
    @OneToMany(orphanRemoval = true, mappedBy = "requisitions")
    @JsonManagedReference
    private List<RequisitionsActionEntity> actions;
}

package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name="part_list")
@Entity
@Data
@RequiredArgsConstructor
public class PartListEntity extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "workder_id", referencedColumnName = "id",
            insertable = false, updatable = false, nullable = false)
    @NonNull
    private UserEntity workder;

    @ManyToOne
    @JoinColumn(name = "work_order_id", referencedColumnName = "id",
            insertable = false, updatable = false, nullable = false)
    @NonNull
    private WorkOrderEntity workOrder;

    @Column(name = "usage", columnDefinition = "NVARCHAR(200)")
    private String usage;

    @ManyToMany
    @JoinTable(name = "xrf_part_list_equipment",
    joinColumns = { @JoinColumn(name = "part_list_id") },
    inverseJoinColumns = { @JoinColumn(name = "equipment_id") })
    private List<EquipmentEntity> equipments;
}
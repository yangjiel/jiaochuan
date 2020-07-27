package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Table(name="part_list")
@Entity
@Data
@RequiredArgsConstructor
public class PartListEntity extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id",
            insertable = false, updatable = false, nullable = false)
    @NonNull
    private UserEntity worker;

    @ManyToOne
    @JoinColumn(name = "work_order_id", referencedColumnName = "id",
            insertable = false, updatable = false, nullable = false)
    @NonNull
    private WorkOrderEntity workOrder;

    @Column(name = "usage", columnDefinition = "NVARCHAR(200)")
    private String usage;

    @OneToMany(mappedBy = "partList")
    private PartListEquipmentEntity partListEquipment;
}
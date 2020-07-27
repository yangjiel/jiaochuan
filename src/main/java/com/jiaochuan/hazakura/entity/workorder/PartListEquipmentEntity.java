package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Table(name="xrf_part_list_equipment")
@Entity
@Data
@RequiredArgsConstructor
public class PartListEquipmentEntity extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "part_list_id", referencedColumnName = "id",
            insertable = false, updatable = false, nullable = false)
    private PartListEntity partList;

    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "id", nullable = false)
    private EquipmentEntity equipment;

    @Column(name = "count")
    private Integer count;
}

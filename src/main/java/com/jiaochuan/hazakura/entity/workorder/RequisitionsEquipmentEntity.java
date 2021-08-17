package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.*;

import javax.persistence.*;


@Table(name = "xrf_requisitions_equipment")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class RequisitionsEquipmentEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requisitions_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @NonNull
    @ToString.Exclude
    private RequisitionsEntity requisitions;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @NonNull
    private EquipmentEntity equipment;
}

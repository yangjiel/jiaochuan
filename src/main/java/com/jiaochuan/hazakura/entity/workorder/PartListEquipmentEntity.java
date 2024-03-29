package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Table(name = "xrf_part_list_equipment")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class PartListEquipmentEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "part_list_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private PartListEntity partList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private EquipmentEntity equipment;
}

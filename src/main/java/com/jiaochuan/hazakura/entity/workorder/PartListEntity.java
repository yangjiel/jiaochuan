package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name="part_list")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class PartListEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity worker;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_order_id", referencedColumnName = "id", nullable = false)
    @NonNull
    @JsonBackReference
    private WorkOrderEntity workOrder;

    @Column(name = "usage", columnDefinition = "NVARCHAR(200)")
    private String usage;

    @OneToMany(mappedBy = "partList")
    private List<PartListEquipmentEntity> partListEquipments;
}
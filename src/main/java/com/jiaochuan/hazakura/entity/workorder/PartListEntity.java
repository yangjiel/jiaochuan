package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "part_list")
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

    @Column(name = "part_list_status", columnDefinition = "VARCHAR(32)", nullable = false)
    @NonNull
    private PartListStatus partListStatus;

    @Column(name = "create_date", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Column(name = "usage", columnDefinition = "VARCHAR(200)")
    private String usage;

    @OneToMany(orphanRemoval = true, mappedBy = "partList")
    @JsonManagedReference
    private List<PartListEquipmentEntity> partListEquipments;

    @JsonProperty("partListActions")
    @OneToMany(orphanRemoval = true, mappedBy = "partList")
    @JsonManagedReference
    private List<PartListActionEntity> actions;
}

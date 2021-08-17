package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Table(name = "work_order_action")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class WorkOrderActionEntity extends ActionEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_order_id", referencedColumnName = "id", nullable = false)
    @NonNull
    @JsonBackReference
    @ToString.Exclude
    private WorkOrderEntity workOrder;

    @Column(name = "prev_status", columnDefinition = "VARCHAR(32)")
    @NonNull
    private Status prevStatus;

    @Column(name = "status", columnDefinition = "VARCHAR(32)")
    @NonNull
    private Status status;
}

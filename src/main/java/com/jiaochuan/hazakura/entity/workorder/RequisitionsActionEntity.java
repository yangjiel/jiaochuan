package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Table(name = "requisitions_action")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class RequisitionsActionEntity extends ActionEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requisition_id", referencedColumnName = "id", nullable = false)
    @NonNull
    @JsonBackReference
    @ToString.Exclude
    private RequisitionsEntity requisitions;

    @Column(name = "prev_status", columnDefinition = "BOOLEAN")
    @NonNull
    private RequisitionsStatus prevStatus;

    @Column(name = "status", columnDefinition = "BOOLEAN")
    @NonNull
    private RequisitionsStatus status;
}

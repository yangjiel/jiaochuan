package com.jiaochuan.hazakura.entity.workorder;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Table(name = "inspection_action")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class InspectionActionEntity extends ActionEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inspection_id", referencedColumnName = "id", nullable = false)
    @NonNull
    @JsonBackReference
    @ToString.Exclude
    private InspectionEntity inspection;

    @Column(name = "prev_status", columnDefinition = "BOOLEAN")
    @NonNull
    private InspectionStatus prevStatus;

    @Column(name = "status", columnDefinition = "BOOLEAN")
    @NonNull
    private InspectionStatus status;
}

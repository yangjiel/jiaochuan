package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Table(name = "part_list_action")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class PartListActionEntity extends ActionEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "part_list_id", referencedColumnName = "id", nullable = false)
    @NonNull
    @JsonBackReference
    private PartListEntity partList;

    @Column(name = "prev_status", columnDefinition = "BOOLEAN")
    @NonNull
    private PartListStatus prevStatus;

    @Column(name = "status", columnDefinition = "BOOLEAN")
    @NonNull
    private PartListStatus status;
}

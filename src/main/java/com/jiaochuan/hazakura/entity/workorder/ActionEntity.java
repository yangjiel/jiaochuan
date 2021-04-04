package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "action")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class ActionEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity user;

    @Column(name = "type", columnDefinition = "VARCHAR(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private ActionType type;

    @Column(name = "status", columnDefinition = "BOOLEAN")
    @NonNull
    private Boolean status;

    @Column(name = "date", columnDefinition = "TIMESTAMP", nullable = false)
    @NonNull
    private Instant date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_order_id", referencedColumnName = "id", nullable = false)
    private WorkOrderEntity workOrder;
}

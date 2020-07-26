package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Table(name="action")
@Entity
@Data
@RequiredArgsConstructor
public class ActionEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id",
                insertable = false, updatable = false, nullable = false)
    @NonNull
    public UserEntity user;

    @Column(name = "type", columnDefinition = "NVARCHAR(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    public ActionType type;

    @Column(name = "status", columnDefinition = "BOOLEAN")
    @NonNull
    public Boolean status;

    @Column(name = "date", columnDefinition = "TIMESTAMP", nullable = false)
    @NonNull
    public Instant date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_order_id", referencedColumnName = "id", nullable = false)
    public WorkOrderEntity workOrder;
}
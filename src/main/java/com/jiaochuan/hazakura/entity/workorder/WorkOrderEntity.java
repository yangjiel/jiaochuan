package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Table(name="work_order")
@Entity
@Data
@RequiredArgsConstructor
public class WorkOrderEntity extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NonNull
    public UserEntity user;

    @Column(name = "reported_date", columnDefinition = "TIMESTAMP")
    @NonNull
    public Instant reportedDate;

    @Column(name = "address", columnDefinition = "NVARCHAR")
    public String address;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public UserEntity worker;

    @Column(name = "evaluation_score", columnDefinition = "TINYINT")
    public Byte evaluationScore;
}

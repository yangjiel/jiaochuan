package com.jiaochuan.hazakura.entity.WorkOrder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.User.UserEntity;
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

    @Column(name = "submitted_date", columnDefinition = "TIMESTAMP")
    @NonNull
    public Instant submittedDate;

    @Column(name = "approved_date", columnDefinition = "TIMESTAMP")
    public Instant approvedDate;

    @Column(name = "serve_date", columnDefinition = "TIMESTAMP")
    public Instant serveDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public UserEntity worker;

    //public GPS gps;

    @Column(name = "evaluation_score", columnDefinition = "TINYINT")
    public Byte evaluationScore;
}

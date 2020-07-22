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

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NonNull
    public UserEntity worker;

    @Column(name = "equitment_id", columnDefinition = "INTEGER")
    @NonNull
    public Instant equitment;

    @Column(name = "service_date", columnDefinition = "TIMESTAMP")
    @NonNull
    public Instant serviceDate;

    @Column(name = "address", columnDefinition = "NVARCHAR")
    public String address;

    @Column(name = "result", columnDefinition = "NVARCHAR")
    public String result;

    @Column(name = "result_description", columnDefinition = "NVARCHAR")
    public String resultDescription;

    @Column(name = "service_item", columnDefinition = "NVARCHAR")
    public String serviceItem;
}

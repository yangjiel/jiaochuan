package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.CustomerEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Table(name="work_order")
@Entity
@Data
@RequiredArgsConstructor
public class WorkOrderEntity extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id",
            insertable = false, updatable = false, nullable = false)
    @NonNull
    public CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id",
            insertable = false, updatable = false, nullable = false)
    @NonNull
    public UserEntity worker;

    @Column(name = "equipment_id", columnDefinition = "INTEGER")
    @NonNull
    public Instant equipment;

    @Column(name = "service_date", columnDefinition = "DATE")
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "workOrder")
    public List<ActionEntity> actions;
}

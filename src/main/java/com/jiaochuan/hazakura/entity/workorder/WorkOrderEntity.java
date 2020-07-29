package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.CustomerEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Table(name="work_order")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class WorkOrderEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private CustomerEntity customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity worker;

    @Column(name = "service_date", columnDefinition = "DATE")
    @NonNull
    private LocalDate serviceDate;

    @Column(name = "address", columnDefinition = "NVARCHAR")
    private String address;

    @Column(name = "result", columnDefinition = "NVARCHAR")
    private String result;

    @Column(name = "result_description", columnDefinition = "NVARCHAR")
    private String resultDescription;

    @Column(name = "service_item", columnDefinition = "NVARCHAR")
    private String serviceItem;

    @OneToMany(orphanRemoval = true, mappedBy = "workOrder")
    private List<ActionEntity> actions;

    @OneToMany(orphanRemoval = true, mappedBy = "workOrder")
    @JsonManagedReference
    private List<PartListEntity> partLists;
}

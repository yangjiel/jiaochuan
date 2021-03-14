package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
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
@JsonPropertyOrder({ "WOid", "client", "worker", "partLists", "status"})
public class WorkOrderEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private ClientEntity client;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity worker;

    @Column(name = "create_date", columnDefinition = "DATE", nullable = false)
    @NonNull
    private LocalDate createDate;

    @Column(name = "address", columnDefinition = "NVARCHAR(100)")
    private String address;

    @Column(name = "status", columnDefinition = "NVARCHAR(32)")
    private Status status;

    @Column(name = "description", columnDefinition = "NVARCHAR(256)")
    private String description;

    @Column(name = "service_item", columnDefinition = "NVARCHAR(256)")
    private String serviceItem;

    @OneToMany(orphanRemoval = true, mappedBy = "workOrder")
    private List<ActionEntity> actions;

    @OneToMany(orphanRemoval = true, mappedBy = "workOrder")
    @JsonManagedReference
    private List<PartListEntity> partLists;

    @JsonProperty("WOid")
    public Long getId() {return this.id;}
}

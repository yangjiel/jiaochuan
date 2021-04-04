package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.*;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "work_order")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@RequiredArgsConstructor
@JsonPropertyOrder({"WOid", "client", "worker", "partLists"})
public class WorkOrderEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private ClientEntity client;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "worker_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity worker;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engineer_id", referencedColumnName = "id")
    private UserEntity engineer;

    @Column(name = "status", columnDefinition = "VARCHAR(32)", nullable = false)
    private Status status;

    @Column(name = "create_date", columnDefinition = "DATETIME", nullable = false)
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Column(name = "address", columnDefinition = "VARCHAR(100)")
    private String address;

    @Column(name = "description", columnDefinition = "VARCHAR(256)")
    private String description;

    @Column(name = "service_item", columnDefinition = "VARCHAR(256)")
    private String serviceItem;

    @OneToMany(orphanRemoval = true, mappedBy = "workOrder")
    private List<ActionEntity> actions;

    @OneToMany(orphanRemoval = true, mappedBy = "workOrder")
    @JsonManagedReference
    private List<PartListEntity> partLists;

    @JsonProperty("WOid")
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public boolean containAllPartListStatus(PartListStatus partListStatus) {
        for (PartListEntity partListEntity : this.getPartLists()) {
            if (partListEntity.getPartListStatus() != partListStatus) {
                return false;
            }
        }
        return true;
    }
}

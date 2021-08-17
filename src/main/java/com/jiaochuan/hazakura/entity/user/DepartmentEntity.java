package com.jiaochuan.hazakura.entity.user;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Table(name = "department")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity extends AbstractEntity {
    @Column(name = "name", columnDefinition = "VARCHAR(32)", nullable = false)
    @NonNull
    private String department;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity leader;
}

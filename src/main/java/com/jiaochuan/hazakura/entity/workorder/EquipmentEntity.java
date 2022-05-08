package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name = "equipment")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentEntity extends AbstractEntity {
    @Column(name = "equipment", columnDefinition = "VARCHAR(64)", nullable = false)
    @NonNull
    private String equipment;

    @Column(name = "model", columnDefinition = "VARCHAR(32)")
    @NonNull
    private String model;

    @Column(name = "quantity", columnDefinition = "INTEGER")
    private Integer quantity;
}

package com.jiaochuan.hazakura.entity.workorder;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name="equipment")
@Entity
@Data
@RequiredArgsConstructor
public class EquipmentEntity {
    @Id
    @Column(name = "id", columnDefinition = "INTEGER")
    public Integer id;

    @Column(name = "name", length = 16)
    @NonNull
    public String name;

    @Column(name = "model", length = 16)
    @NonNull
    public String model;

    @Column(name = "manufacture", length = 16)
    public String manufacture;

    @ManyToMany(mappedBy = "equipments")
    public List<PartListEntity> partList;
}

package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name="equipment")
@Entity
@Data
@RequiredArgsConstructor
public class EquipmentEntity extends AbstractEntity {
    @Column(name = "device_name", columnDefinition = "NVARCHAR(64)", nullable = false)
    @NonNull
    public String deviceName;

    @Column(name = "device_model", columnDefinition = "NVARCHAR(32)")
    @NonNull
    public String deviceModel;

    @Column(name = "manufacture", columnDefinition = "NVARCHAR(100)")
    public String manufacture;

    @ManyToMany(mappedBy = "equipments")
    public List<PartListEntity> partList;
}

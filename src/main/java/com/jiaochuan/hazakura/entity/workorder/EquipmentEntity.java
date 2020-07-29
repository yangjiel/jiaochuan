package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Table(name="equipment")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentEntity extends AbstractEntity {
    @Column(name = "device_name", columnDefinition = "NVARCHAR(64)", nullable = false)
    @NonNull
    private String deviceName;

    @Column(name = "device_model", columnDefinition = "NVARCHAR(32)")
    @NonNull
    private String deviceModel;

    @Column(name = "manufacture", columnDefinition = "NVARCHAR(100)")
    private String manufacture;

    @OneToMany(mappedBy = "equipment")
    private List<PartListEquipmentEntity> partListEquipment;
}

package com.jiaochuan.hazakura.api.workorder;

import com.jiaochuan.hazakura.entity.workorder.EquipmentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetEquipmentResponseDto {
    String status;
    EquipmentEntity equipmentEntity;
}
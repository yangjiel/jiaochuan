package com.jiaochuan.hazakura.api.workorder;

import lombok.Data;

@Data
public class EquipmentDto {
    private String equipment;
    private String model;
    private Integer quantity;
}

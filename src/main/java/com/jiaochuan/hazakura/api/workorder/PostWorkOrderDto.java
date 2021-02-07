package com.jiaochuan.hazakura.api.workorder;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PostWorkOrderDto {
    private Long clientId;
    private Long workerId;
    private LocalDate serviceDate;
    private String address;
    private List<EquipmentDto> equipments;
}

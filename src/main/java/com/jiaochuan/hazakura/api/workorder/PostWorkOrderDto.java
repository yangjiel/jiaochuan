package com.jiaochuan.hazakura.api.workorder;

import com.jiaochuan.hazakura.entity.workorder.Status;
import lombok.Data;

import java.util.List;

@Data
public class PostWorkOrderDto {
    private Long workOrderId;
    private Long clientId;
    private Long workerId;
    private Long engineerId;
    private String address;
    private Status status;
    private String description;
    private String comment;
    private String serviceItem;
    private List<EquipmentDto> equipments;
}

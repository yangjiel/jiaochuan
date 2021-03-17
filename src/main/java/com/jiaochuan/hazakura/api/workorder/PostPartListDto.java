package com.jiaochuan.hazakura.api.workorder;

import lombok.Data;

import java.util.List;

@Data
public class PostPartListDto {
    private Long workerId;
    private Long workOrderId;
    private String usage;
    private Long partListId;
    private List<EquipmentDto> equipments;
}

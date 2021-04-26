package com.jiaochuan.hazakura.api.workorder;

import com.jiaochuan.hazakura.entity.workorder.PartListStatus;
import lombok.Data;

import java.util.List;

@Data
public class PostPartListDto {
    private Long workerId;
    private Long workOrderId;
    private String usage;
    private Long partListId;
    private String comment;
    private PartListStatus partListStatus;
    private List<EquipmentDto> equipments;
}

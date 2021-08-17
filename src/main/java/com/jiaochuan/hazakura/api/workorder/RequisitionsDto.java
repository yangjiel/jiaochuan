package com.jiaochuan.hazakura.api.workorder;
import com.jiaochuan.hazakura.entity.workorder.RequisitionsStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequisitionsDto {
    private Long requisitionsId;
    private Long creatorId;
    private Long purchaserId;
    private Long workOrderId;
    private Long departmentId;
    private Long purchaseOrderId;
    private RequisitionsStatus status;
    private LocalDateTime purchaseDate;
    private String supplier;
    private String comment;
    private List<EquipmentDto> equipments;
}

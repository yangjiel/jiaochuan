package com.jiaochuan.hazakura.api.workorder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
class WorkOrderListDto {
    private List<PostWorkOrderDto> WorkOrderList;
    private String message;
}

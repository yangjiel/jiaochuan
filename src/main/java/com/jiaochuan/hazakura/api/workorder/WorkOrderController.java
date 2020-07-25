package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.workorder.WorkOrderEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/work-order")
public class WorkOrderController {
    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createWorkOrder(@RequestBody String jsonRequest) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WorkOrderEntity workOrderEntity = objectMapper.convertValue(jsonRequest, WorkOrderEntity.class);
            workOrderService.createWorkOrder(workOrderEntity);
            return ResponseEntity.ok().build();
        } catch (AppException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}

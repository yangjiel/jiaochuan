package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.WorkOrderEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.WorkOrder.WorkOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

//import com.jiaochuan.hazakura.exception.UserException;

@Service
public class WorkOrderService {
    @Autowired
    private WorkOrderRepository workOrderRepository;

    public void createWorkOrder(WorkOrderEntity workOrderEntity) throws AppException, Exception {
        // Check if required fields are not empty
        try {
            for (Field field : workOrderEntity.getClass().getDeclaredFields()) {
                if (!field.trySetAccessible()) {
                    throw new AppException("创建工单时无法取得WorkOrderEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(workOrderEntity))) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                } else if (field.get(workOrderEntity) == null) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch(IllegalAccessException e) {
            throw new AppException("创建工单时无法取得WorkOrderEntity的反射访问权限，其成员变量无法通过反射访问。");
        }

        // Check format for cell phone and email
        if (workOrderEntity.getAddress().length() > 64) {
            throw new Exception("地址长度不能多于11位。");
        }

        workOrderRepository.save(workOrderEntity);
    }

    public List<WorkOrderEntity> getWorkOrders(int page, int size) throws Exception {
        if (page < 0 || size < 0) {
            throw new Exception("分页设置不能小于0。");
        }
        return workOrderRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

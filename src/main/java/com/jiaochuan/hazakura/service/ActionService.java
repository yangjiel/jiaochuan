package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.WorkOrderActionEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.WorkOrder.WorkOrderActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ActionService {
    @Autowired
    private WorkOrderActionRepository workOrderActionRepository;

    @Transactional
    public void createAction(WorkOrderActionEntity workOrderActionEntity) throws AppException, Exception {
        // Check if required fields are not empty
        Helper.checkFields(WorkOrderActionEntity.class, workOrderActionEntity);
    }
}

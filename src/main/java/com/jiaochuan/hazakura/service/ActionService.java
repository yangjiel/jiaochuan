package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.WorkOrderActionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ActionService {
    @Transactional
    public void createAction(WorkOrderActionEntity workOrderActionEntity) throws Exception {
        // Check if required fields are not empty
        Helper.checkFields(WorkOrderActionEntity.class, workOrderActionEntity);
    }
}

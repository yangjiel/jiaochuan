package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.ActionEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.WorkOrder.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ActionService {
    @Autowired
    private ActionRepository actionRepository;

    @Transactional
    public void createAction(ActionEntity actionEntity) throws AppException, Exception {
        // Check if required fields are not empty
        Helper.checkFields(ActionEntity.class, actionEntity);
    }
}

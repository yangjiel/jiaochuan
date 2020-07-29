package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.ActionEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.WorkOrder.ActionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

@Service
public class ActionService {
    @Autowired
    private ActionRepository actionRepository;

    @Transactional
    public void createAction(ActionEntity actionEntity) throws AppException, Exception {
        // Check if required fields are not empty
        try {
            for (Field field : actionEntity.getClass().getDeclaredFields()) {
                if (!field.trySetAccessible()) {
                    throw new AppException("创建用户时无法取得ActionEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(actionEntity))) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                } else if (field.get(actionEntity) == null) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch (IllegalAccessException e) {
            throw new AppException("创建用户时无法取得ActionEntity的反射访问权限，其成员变量无法通过反射访问。");
        }
    }
}

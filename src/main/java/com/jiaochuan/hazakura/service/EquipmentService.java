package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.EquipmentEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.WorkOrder.EquipmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    public void createEquipment(EquipmentEntity equipmentEntity) throws AppException, Exception {
        // Check if required fields are not empty
        try {
            for (Field field : equipmentEntity.getClass().getDeclaredFields()) {
                if (!field.trySetAccessible()) {
                    throw new AppException("创建用户时无法取得EquipmentEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(equipmentEntity))) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                } else if (field.get(equipmentEntity) == null) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch (IllegalAccessException e) {
            throw new AppException("创建用户时无法取得EquipmentEntity的反射访问权限，其成员变量无法通过反射访问。");
        }
    }
}

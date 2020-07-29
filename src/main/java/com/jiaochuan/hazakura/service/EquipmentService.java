package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.EquipmentEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.WorkOrder.EquipmentRepository;
import io.sentry.event.User;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Transactional
    public void createEquipment(EquipmentEntity equipmentEntity) throws AppException, UserException {
        // Check if required fields are not empty
        try {
            for (Field field : equipmentEntity.getClass().getDeclaredFields()) {
                if (field.getAnnotation(NonNull.class) == null) {
                    continue;
                }
                if (!field.trySetAccessible()) {
                    throw new AppException("创建用户时无法取得EquipmentEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(equipmentEntity))) {
                    throw new UserException("必填项" + field.getName() + "不能为空。");
                } else if (field.get(equipmentEntity) == null) {
                    throw new UserException("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch (IllegalAccessException e) {
            throw new AppException("创建用户时无法取得EquipmentEntity的反射访问权限，其成员变量无法通过反射访问。");
        }

        equipmentRepository.save(equipmentEntity);
    }

    public List<EquipmentEntity> getEquipments(int page, int size) throws Exception {
        if (page < 0 || size < 0) {
            throw new Exception("分页设置不能小于0。");
        }
        return equipmentRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

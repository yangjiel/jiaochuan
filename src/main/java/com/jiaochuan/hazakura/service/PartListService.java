package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class PartListService {
    @Autowired
    private PartListRepository PartListRepository;

    public void createPartList(PartListEntity partListEntity) throws AppException, Exception {
        // Check if required fields are not empty
        try {
            for (Field field : partListEntity.getClass().getDeclaredFields()) {
                if (!field.trySetAccessible()) {
                    throw new AppException("创建用户时无法取得PartListEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(partListEntity))) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                } else if (field.get(partListEntity) == null) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch (IllegalAccessException e) {
            throw new AppException("创建用户时无法取得PartListEntity的反射访问权限，其成员变量无法通过反射访问。");
        }
    }
}
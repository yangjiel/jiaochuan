package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class PartListService {
    @Autowired
    private PartListRepository partListRepository;

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

    public List<PartListEntity> getPartLists(int page, int size) throws Exception {
        if (page < 0 || size < 0) {
            throw new Exception("分页设置不能小于0。");
        }
        return partListRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}
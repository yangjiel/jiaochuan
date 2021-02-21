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

import java.util.List;

@Service
public class EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Transactional
    public void createEquipment(EquipmentEntity equipmentEntity) throws AppException, UserException {
        // Check if required fields are not empty
        Helper.checkFields(EquipmentEntity.class, equipmentEntity);

        equipmentRepository.save(equipmentEntity);
    }

    public List<EquipmentEntity> getEquipments(int page, int size) throws Exception {
        if (page < 0 || size < 0) {
            throw new Exception("分页设置不能小于0。");
        }
        return equipmentRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

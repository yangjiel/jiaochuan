package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentDto;
import com.jiaochuan.hazakura.api.workorder.PostPartListDto;
import com.jiaochuan.hazakura.entity.workorder.EquipmentEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEquipmentEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.WorkOrder.EquipmentRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListEquipmentRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentService {
    @Autowired
    private PartListRepository partListRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private PartListEquipmentRepository partListEquipmentRepository;

    @Transactional
    public PartListEntity createEquipment(PostPartListDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        PartListEntity partListEntity = partListRepository.findById(dto.getPartListId()).orElse(null);
        if (partListEntity != null) {
            List<PartListEquipmentEntity> xrfList = new ArrayList<>();
            if (dto.getEquipments() != null) {
                for (EquipmentDto equipmentPair : dto.getEquipments()) {
                    EquipmentEntity equipmentEntity = new EquipmentEntity(
                            equipmentPair.getEquipment(),
                            equipmentPair.getModel(),
                            equipmentPair.getQuantity());
                    equipmentRepository.save(equipmentEntity);
                    PartListEquipmentEntity xrf = new PartListEquipmentEntity(partListEntity, equipmentEntity);
                    partListEquipmentRepository.save(xrf);
                    xrfList.add(xrf);
                }
            }
            partListEntity.setPartListEquipments(xrfList);
            partListRepository.save(partListEntity);
        }

        return partListEntity;
    }

}

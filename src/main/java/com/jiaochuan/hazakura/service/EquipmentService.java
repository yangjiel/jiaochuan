package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentDto;
import com.jiaochuan.hazakura.api.workorder.PostPartListDto;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEquipmentEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
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
    private PartListEquipmentRepository partListEquipmentRepository;

    @Transactional
    public PartListEntity createEquipment(PostPartListDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        PartListEntity partListEntity = partListRepository.findById(dto.getPartListId()).orElse(null);
        if (partListEntity != null) {
            List<PartListEquipmentEntity> xrfList = new ArrayList<>();
            if (dto.getEquipments() != null) {
                for (EquipmentDto equipmentPair : dto.getEquipments()) {
                    String equipment = equipmentPair.getEquipment();
                    String model = equipmentPair.getModel();
                    Integer quantity = equipmentPair.getQuantity();
                    PartListEquipmentEntity xrf = new PartListEquipmentEntity(partListEntity, equipment, model, quantity);
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

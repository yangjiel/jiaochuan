package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentDto;
import com.jiaochuan.hazakura.api.workorder.RequisitionsDto;
import com.jiaochuan.hazakura.entity.user.DepartmentEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.*;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.DepartmentRepository;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RequisitionsService {
    @Autowired
    private RequisitionsRepository requisitionsRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private RequisitionsEquipmentRepository requisitionsEquipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private RequisitionsActionRepository requisitionsActionRepository;

    @Transactional
    public RequisitionsEntity createRequisitions(RequisitionsDto dto) throws AppException, UserException {
        Set<String> mandatoryFieldsSet = Set.of("creatorId", "workOrderId", "departmentId");
        Helper.checkFields(RequisitionsDto.class, dto, mandatoryFieldsSet);

        WorkOrderEntity workOrderEntity = workOrderRepository.findById(dto.getWorkOrderId()).orElse(null);
        DepartmentEntity departmentEntity = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
        if (workOrderEntity == null) {
            throw new UserException(String.format("ID为%s的工单不存在！", dto.getWorkOrderId()));
        }

        if (departmentEntity == null) {
            throw new UserException(String.format("ID为%s的部门不存在！", dto.getDepartmentId()));
        }

        UserEntity creatorEntity = userRepository.findById(dto.getCreatorId()).orElse(null);
        if (creatorEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在！", dto.getCreatorId()));
        }

        RequisitionsEntity requisitionsEntity = new RequisitionsEntity(
                creatorEntity,
                workOrderEntity,
                departmentEntity,
                RequisitionsStatus.PENDING_PURCHASE,
                LocalDateTime.now());
        List<RequisitionsEquipmentEntity> xrfList = new ArrayList<>();
        if (dto.getEquipments() != null) {
            for (EquipmentDto equipmentPair : dto.getEquipments()) {
                String equipment = equipmentPair.getEquipment();
                String model = equipmentPair.getModel();
                Integer quantity = equipmentPair.getQuantity();
                EquipmentEntity equipmentEntity = new EquipmentEntity(equipment, model, quantity);
                RequisitionsEquipmentEntity xrf = new RequisitionsEquipmentEntity(requisitionsEntity, equipmentEntity);
                equipmentRepository.save(equipmentEntity);
                requisitionsEquipmentRepository.save(xrf);
                xrfList.add(xrf);
            }
        }
        requisitionsEntity.setEquipments(xrfList);
        RequisitionsActionEntity requisitionsActionEntity = new RequisitionsActionEntity(requisitionsEntity,
                RequisitionsStatus.PENDING_PURCHASE, RequisitionsStatus.PENDING_PURCHASE);
        requisitionsActionEntity.setUser(creatorEntity);
        requisitionsActionEntity.setDate(LocalDateTime.now());
        List<RequisitionsActionEntity> requisitionsActionEntityList = new ArrayList<>();
        requisitionsActionEntityList.add(requisitionsActionEntity);
        requisitionsEntity.setActions(requisitionsActionEntityList);
        requisitionsRepository.save(requisitionsEntity);
        return requisitionsEntity;
    }

    @Transactional
    public RequisitionsEntity updateRequisitions(Long userId, RequisitionsDto dto) throws UserException {
        RequisitionsEntity requisitionsEntity = requisitionsRepository.findById(dto.getRequisitionsId())
                .orElse(null);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (requisitionsEntity == null) {
            throw new UserException("采购单不存在！");
        }
        if (userEntity == null) {
            throw new UserException("用户不存在！");
        }

        requisitionsEntity.setPurchaser(userRepository.findById(dto.getPurchaserId()).orElse(null));
        requisitionsEntity.setPurchaseOrderId(dto.getPurchaseOrderId());
        requisitionsEntity.setPurchaseDate(dto.getPurchaseDate());
        requisitionsEntity.setSupplier(dto.getSupplier());

        if (dto.getStatus() != null) {
            RequisitionsActionEntity requisitionsActionEntity = new RequisitionsActionEntity(requisitionsEntity,
                    requisitionsEntity.getStatus(),
                    dto.getStatus());
            requisitionsActionEntity.setUser(userEntity);
            requisitionsActionEntity.setComment(dto.getComment());
            requisitionsActionEntity.setDate(LocalDateTime.now());
            requisitionsActionRepository.save(requisitionsActionEntity);
            requisitionsEntity.setStatus(dto.getStatus());
            List<RequisitionsActionEntity> requisitionsActionEntityList = requisitionsEntity.getActions();
            requisitionsActionEntityList.add(requisitionsActionEntity);
            requisitionsEntity.setActions(requisitionsActionEntityList);
        }
        requisitionsRepository.save(requisitionsEntity);
        return requisitionsEntity;
    }
}

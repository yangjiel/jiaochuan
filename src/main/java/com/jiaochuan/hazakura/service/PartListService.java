package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentDto;
import com.jiaochuan.hazakura.api.workorder.PostPartListDto;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.*;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
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
public class PartListService {
    @Autowired
    private PartListRepository partListRepository;

    @Autowired
    private PartListEquipmentRepository partListEquipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private PartListActionRepository partListActionRepository;

    @Transactional
    public void addActionHistory(PartListEntity partListEntity,
                                 PartListStatus partListStatus,
                                 UserEntity user,
                                 String comment) {
        PartListActionEntity partListActionEntity = new PartListActionEntity(partListEntity,
                partListEntity.getPartListStatus(), partListStatus);
        partListActionEntity.setUser(user);
        partListActionEntity.setComment(comment);
        partListActionEntity.setDate(LocalDateTime.now());
        partListActionRepository.save(partListActionEntity);
        List<PartListActionEntity> partListActionEntityList = partListEntity.getActions();
        partListActionEntityList.add(partListActionEntity);
        partListEntity.setActions(partListActionEntityList);
    }

    @Transactional
    public PartListEntity createPartList(PostPartListDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        Set<String> mandatoryFieldsSet = Set.of("workerId", "workOrderId");
        Helper.checkFields(PostPartListDto.class, dto, mandatoryFieldsSet);

        WorkOrderEntity workOrderEntity = workOrderRepository.findById(dto.getWorkOrderId()).orElse(null);
        if (workOrderEntity == null) {
            throw new UserException(String.format("ID为%s的工单不存在！", dto.getWorkOrderId()));
        }

        UserEntity workerEntity = userRepository.findById(dto.getWorkerId()).orElse(null);
        if (workerEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在！", dto.getWorkerId()));
        }
        if (dto.getUsage().length() > 200) {
            throw new UserException("领料单备注长度不能大于200个字符！");
        }
//        if (workOrderEntity.getPartLists().size() != 0) {
//            throw new UserException("该工单已存在初次领料单，请申请二次领料单！");
//        }
        if (workOrderEntity.getPartLists().size() > 0) {
            throw new UserException("目前版本尚未支持二次领料单！");
        }
        PartListEntity partListEntity = createPartListHelper(workerEntity,
                workOrderEntity,
                PartListStatus.PENDING_FINALIZE,
                dto.getEquipments());
        workOrderEntity.getPartLists().add(partListEntity);
        workOrderRepository.save(workOrderEntity);
        return partListEntity;

    }

    @Transactional
    public PartListEntity createPartListHelper(UserEntity workerEntity,
                                               WorkOrderEntity workOrderEntity,
                                               PartListStatus status,
                                               List<EquipmentDto> equipments) {

        PartListEntity partListEntity = new PartListEntity(workerEntity, workOrderEntity, status);
        List<PartListEquipmentEntity> xrfList = new ArrayList<>();
        if (equipments != null) {
            for (EquipmentDto equipmentPair : equipments) {
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
        PartListActionEntity partListActionEntity = new PartListActionEntity(partListEntity,
                status, status);
        partListActionEntity.setUser(workerEntity);
        partListActionEntity.setDate(LocalDateTime.now());
        partListActionRepository.save(partListActionEntity);
        List<PartListActionEntity> partListActionEntityList = new ArrayList<>();
        partListActionEntityList.add(partListActionEntity);
        partListEntity.setActions(partListActionEntityList);
        partListEntity.setPartListEquipments(xrfList);
        partListRepository.save(partListEntity);
        return partListEntity;
    }

    @Transactional
    public PartListEntity updatePartListStatusHelper(Long userId,
                                                     PostPartListDto dto) throws UserException {
        PartListEntity partListEntity = partListRepository.findById(dto.getPartListId()).orElse(null);
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (partListEntity == null) {
            throw new UserException("领料单不存在！");
        }
        if (user == null) {
            throw new UserException("用户不存在！");
        }
        if ((user.getRole() == Role.MANAGER_PROCUREMENT &&
                dto.getPartListStatus() != PartListStatus.APPROVED) ||
                (user.getRole() == Role.STAFF_INVENTORY &&
                        dto.getPartListStatus() != PartListStatus.READY)) {
            throw new UserException("该用户无权设置该领料单状态！");
        }
        if (dto.getPartListStatus() != null) {
            addActionHistory(partListEntity, dto.getPartListStatus(), user, dto.getComment());
            partListEntity.setPartListStatus(dto.getPartListStatus());
            if (dto.getPartListStatus() == PartListStatus.READY &&
                    partListEntity.getWorkOrder().containAllPartListStatus(PartListStatus.READY)) {
                partListEntity.getWorkOrder().setStatus(WorkOrderStatus.PROCEEDING);
                workOrderRepository.save(partListEntity.getWorkOrder());
            }
            partListRepository.save(partListEntity);
        }
        if (dto.getWorkerId() != null) {
            UserEntity worker = userRepository.findById(dto.getWorkerId()).orElse(null);
            if (worker != null) {
                partListEntity.setWorker(worker);
                partListRepository.save(partListEntity);
            }
        }
        return partListEntity;
    }
}

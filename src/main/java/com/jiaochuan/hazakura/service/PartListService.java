package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentDto;
import com.jiaochuan.hazakura.api.workorder.PostPartListDto;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.*;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListEquipmentRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    private EntityManager em;

    @Transactional
    public PartListEntity createPartList(PostPartListDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        Set<String> mandatoryFieldsSet = Set.of("workerId", "workOrderId");
        Helper.checkFields(PostPartListDto.class, dto, mandatoryFieldsSet);

        WorkOrderEntity workOrderEntity = workOrderRepository.findById(dto.getWorkOrderId()).orElse(null);
        if (workOrderEntity == null) {
            throw new UserException(String.format("ID为%s的工单不存在。", dto.getWorkOrderId()));
        }

        UserEntity workerEntity = userRepository.findById(dto.getWorkerId()).orElse(null);
        if (workerEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在。", dto.getWorkerId()));
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
            throw new UserException("该用户无权设置该领料单状态");
        }
        if (dto.getPartListStatus() != null) {
            partListEntity.setPartListStatus(dto.getPartListStatus());
            if (dto.getPartListStatus() == PartListStatus.READY &&
                    partListEntity.getWorkOrder().containAllPartListStatus(PartListStatus.READY)) {
                partListEntity.getWorkOrder().setStatus(Status.PROCEEDING);
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

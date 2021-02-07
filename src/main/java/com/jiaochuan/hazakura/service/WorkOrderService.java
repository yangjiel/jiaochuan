package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentDto;
import com.jiaochuan.hazakura.api.workorder.PostWorkOrderDto;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.EquipmentEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEquipmentEntity;
import com.jiaochuan.hazakura.entity.workorder.WorkOrderEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.ClientRepository;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.EquipmentRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListEquipmentRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.WorkOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WorkOrderService {
    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartListRepository partListRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private PartListEquipmentRepository partListEquipmentRepository;

    @Transactional
    public void createWorkOrder(PostWorkOrderDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        Set<String> mandatoryFieldsSet = Set.of("clientId", "workerId", "serviceDate", "address");

        try {
            for (Field field : dto.getClass().getDeclaredFields()) {
                if (!field.trySetAccessible()) {
                    throw new AppException("创建工单时无法取得WorkOrderEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (!mandatoryFieldsSet.contains(field.getName())) {
                    continue;
                }


                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(dto))) {
                    throw new UserException("必填项" + field.getName() + "不能为空。");
                } else if (field.get(dto) == null) {
                    throw new UserException("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch(IllegalAccessException e) {
            throw new AppException("创建工单时无法取得WorkOrderEntity的反射访问权限，其成员变量无法通过反射访问。");
        }

        ClientEntity clientEntity = clientRepository.findById(dto.getClientId()).orElse(null);
        if (clientEntity == null) {
            throw new UserException(String.format("ID为%s的客户不存在。", dto.getClientId()));
        }

        UserEntity workerEntity = userRepository.findById(dto.getWorkerId()).orElse(null);
        if (workerEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在。", dto.getClientId()));
        }

        WorkOrderEntity workOrderEntity = new WorkOrderEntity(clientEntity, workerEntity, dto.getServiceDate());
        workOrderEntity.setAddress(dto.getAddress());
        PartListEntity partListEntity = createPartList(workerEntity, workOrderEntity, dto.getEquipments());

        List<PartListEntity> partLists = new ArrayList<>();
        partLists.add(partListEntity);
        workOrderEntity.setPartLists(partLists);
        workOrderRepository.save(workOrderEntity);
    }

    public List<WorkOrderEntity> getWorkOrders(int page, int size) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        return workOrderRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Transactional
    public PartListEntity createPartList(UserEntity workerEntity, WorkOrderEntity workOrderEntity,
                               List<EquipmentDto> equipments) throws UserException {

        PartListEntity partListEntity = new PartListEntity(workerEntity, workOrderEntity);
        List<PartListEquipmentEntity> xrfList = new ArrayList<>();
        if (equipments != null) {
            for (EquipmentDto equipmentPair : equipments) {
                Long equipmentId = equipmentPair.getEquipmentId();
                Integer quantity = equipmentPair.getQuantity();
                EquipmentEntity equipmentEntity = equipmentRepository.findById(equipmentId).orElse(null);
                if (equipmentEntity == null) {
                    throw new UserException(String.format("ID为%s的设备不存在。", equipmentId));
                }
                PartListEquipmentEntity xrf = new PartListEquipmentEntity(partListEntity, equipmentEntity, quantity);
                partListEquipmentRepository.save(xrf);
                xrfList.add(xrf);
            }
        }
        partListEntity.setPartListEquipments(xrfList);
        partListRepository.save(partListEntity);
        return partListEntity;
    }
}

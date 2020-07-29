package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentRequestDto;
import com.jiaochuan.hazakura.api.workorder.WorkOrderCreateRequestDto;
import com.jiaochuan.hazakura.entity.user.CustomerEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.EquipmentEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEquipmentEntity;
import com.jiaochuan.hazakura.entity.workorder.WorkOrderEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.CustomerRepository;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.EquipmentRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListEquipmentRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.PartListRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.WorkOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WorkOrderService {
    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartListRepository partListRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private PartListEquipmentRepository partListEquipmentRepository;

    public void createWorkOrder(WorkOrderCreateRequestDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        Set<String> mandatoryFieldsSet = Set.of("customerId", "workerId", "serviceDate", "address");

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

        CustomerEntity customerEntity = customerRepository.findById(dto.getCustomerId()).orElse(null);
        if (customerEntity == null) {
            throw new UserException(String.format("ID为%s的客户不存在。", dto.getCustomerId()));
        }

        UserEntity workerEntity = userRepository.findById(dto.getWorkerId()).orElse(null);
        if (workerEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在。", dto.getCustomerId()));
        }

        WorkOrderEntity workOrderEntity = new WorkOrderEntity(customerEntity, workerEntity, dto.getServiceDate());
        workOrderEntity.setAddress(dto.getAddress());
        workOrderRepository.save(workOrderEntity);
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

    public PartListEntity createPartList(UserEntity workerEntity, WorkOrderEntity workOrderEntity,
                               List<EquipmentRequestDto> equipments) throws UserException {

        PartListEntity partListEntity = new PartListEntity(workerEntity, workOrderEntity);
        List<PartListEquipmentEntity> xrfList = new ArrayList<>();
        if (equipments != null) {
            for (EquipmentRequestDto equipmentPair : equipments) {
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

package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.EquipmentDto;
import com.jiaochuan.hazakura.api.workorder.PostWorkOrderDto;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
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

    @Autowired
    private EntityManager em;

    @Transactional
    public void createWorkOrder(PostWorkOrderDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        Set<String> mandatoryFieldsSet = Set.of("clientId", "workerId", "serviceDate", "address");
        Helper.checkFields(PostWorkOrderDto.class, dto, mandatoryFieldsSet);

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

    public List<WorkOrderEntity> getWorkOrders(int page, int size, ClientEntity client, UserEntity worker, LocalDate date, String result) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WorkOrderEntity> cq = cb.createQuery(WorkOrderEntity.class);

        Root<WorkOrderEntity> workOrder = cq.from(WorkOrderEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (client != null) {
            predicates.add(cb.equal(workOrder.get("client_id"), client));
        }
        if (worker != null) {
            predicates.add(cb.equal(workOrder.get("worker_id"), worker));
        }
        if (date != null) {
            predicates.add(cb.equal(workOrder.get("service_date"), date));
        }
        if (result != null) {
            predicates.add(cb.like(workOrder.get("result"), "%" + result + "%"));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    @Transactional
    public PartListEntity createPartList(UserEntity workerEntity, WorkOrderEntity workOrderEntity,
                               List<EquipmentDto> equipments) throws UserException {

        PartListEntity partListEntity = new PartListEntity(workerEntity, workOrderEntity);
        List<PartListEquipmentEntity> xrfList = new ArrayList<>();
        if (equipments != null) {
            for (EquipmentDto equipmentPair : equipments) {
                String equipment = equipmentPair.getEquipment();
                Integer quantity = equipmentPair.getQuantity();
                PartListEquipmentEntity xrf = new PartListEquipmentEntity(partListEntity, equipment, quantity);
                partListEquipmentRepository.save(xrf);
                xrfList.add(xrf);
            }
        }
        partListEntity.setPartListEquipments(xrfList);
        partListRepository.save(partListEntity);
        return partListEntity;
    }
}

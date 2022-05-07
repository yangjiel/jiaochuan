package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.PostWorkOrderDto;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.*;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.ClientRepository;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WorkOrderService extends PartListService {
    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PartListRepository partListRepository;

    @Autowired
    private WorkOrderActionRepository workOrderActionRepository;

    @Autowired
    private EntityManager em;

    @Transactional
    public WorkOrderEntity createWorkOrder(PostWorkOrderDto dto) throws AppException, UserException {
        // Check if required fields are not empty
        Set<String> mandatoryFieldsSet = Set.of("clientId", "workerId");
        Helper.checkFields(PostWorkOrderDto.class, dto, mandatoryFieldsSet);

        ClientEntity clientEntity = clientRepository.findById(dto.getClientId()).orElse(null);
        if (clientEntity == null) {
            throw new UserException(String.format("ID为%s的客户不存在。", dto.getClientId()));
        }

        UserEntity workerEntity = userRepository.findById(dto.getWorkerId()).orElse(null);
        UserEntity engineerEntity = null;
        if (dto.getEngineerId() != null) {
            engineerEntity = userRepository.findById(dto.getEngineerId()).orElse(null);
        }
        if (workerEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在。", dto.getClientId()));
        }
        if (dto.getAddress().length() > 100) {
            throw new UserException("工单地址长度不能大于100个字符！");
        }
        if (dto.getDescription().length() > 256) {
            throw new UserException("工单备注长度不能大于256个字符！");
        }
        if (dto.getServiceItem().length() > 256) {
            throw new UserException("工单服务内容长度不能大于256个字符！");
        }

        WorkOrderEntity workOrderEntity = new WorkOrderEntity(clientEntity, workerEntity, LocalDateTime.now());
        if (engineerEntity != null) {
            workOrderEntity.setEngineer(engineerEntity);
        }
        workOrderEntity.setAddress(dto.getAddress());
        workOrderEntity.setStatus(dto.getStatus() != null ? dto.getStatus() : WorkOrderStatus.PENDING_FIRST_APPROVAL);
        workOrderEntity.setDescription(dto.getDescription());
        workOrderEntity.setServiceItem(dto.getServiceItem());
        WorkOrderActionEntity workOrderActionEntity = new WorkOrderActionEntity(workOrderEntity,
                dto.getStatus(), dto.getStatus());
        workOrderActionEntity.setUser(workerEntity);
        workOrderActionEntity.setComment(dto.getComment());
        workOrderActionEntity.setDate(LocalDateTime.now());
        workOrderActionRepository.save(workOrderActionEntity);
        List<WorkOrderActionEntity> workOrderActionEntityList = new ArrayList<>();
        workOrderActionEntityList.add(workOrderActionEntity);
        workOrderEntity.setActions(workOrderActionEntityList);
        PartListEntity partListEntity;
        partListEntity = createPartListHelper(workerEntity,
                workOrderEntity,
                PartListStatus.PENDING_FINALIZE,
                dto.getEquipments());
        List<PartListEntity> partLists = new ArrayList<>();
        partLists.add(partListEntity);
        workOrderEntity.setPartLists(partLists);

        workOrderRepository.save(workOrderEntity);
        return workOrderEntity;
    }

    public List<WorkOrderEntity> getWorkOrders(int page,
                                               int size,
                                               ClientEntity client,
                                               UserEntity worker,
                                               LocalDateTime datetime,
                                               WorkOrderStatus workOrderStatus,
                                               PartListStatus partListStatus,
                                               String orderBy) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        final String clientStr = "client";
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WorkOrderEntity> cq = cb.createQuery(WorkOrderEntity.class);

        Root<WorkOrderEntity> workOrder = cq.from(WorkOrderEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (client != null) {
            predicates.add(cb.equal(workOrder.get(clientStr), client));
        }
        if (worker != null) {
            predicates.add(cb.equal(workOrder.get("worker"), worker));
        }
        if (datetime != null) {
            predicates.add(cb.equal(workOrder.get("createDate"), datetime));
        }
        if (workOrderStatus != null) {
            predicates.add(cb.equal(workOrder.get("status"), workOrderStatus));
        }
        if (partListStatus != null) {
            predicates.add(cb.equal(workOrder.join("partLists").get("partListStatus"), partListStatus));
        }

        switch (orderBy != null ? orderBy : "") {
            case "timeAsc":
                cq.orderBy(cb.asc(workOrder.get("id")));
                break;
            case "nameDesc":
                cq.orderBy(cb.desc(cb.function(
                        "convertEncode",
                        String.class,
                        workOrder.join(clientStr).get("userName"), cb.literal("gbk"))));
                break;
            case "nameAsc":
                cq.orderBy(cb.asc(cb.function(
                        "convertEncode",
                        String.class,
                        workOrder.join(clientStr).get("userName"), cb.literal("gbk"))));
                break;
            default:
                cq.orderBy(cb.desc(workOrder.get("id")));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        List<WorkOrderEntity> list = em.createQuery(cq).getResultList();
        PagedListHolder<WorkOrderEntity> pagedList = new PagedListHolder<>(list);
        pagedList.setPageSize(size);
        pagedList.setPage(page);
        return pagedList.getPageList();
    }

    @Transactional
    public WorkOrderEntity updateWorkOrderStatusHelper(Long userId,
                                                       PostWorkOrderDto dto) throws UserException {
        WorkOrderEntity workOrderEntity = workOrderRepository.findById(dto.getWorkOrderId()).orElse(null);
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (workOrderEntity == null) {
            throw new UserException("工单不存在！");
        }
        if (user == null) {
            throw new UserException("用户不存在！");
        }
//        if ((user.getRole() == Role.DIRECTOR_AFTER_SALES &&
//                input.getWorkOrderStatus() != WorkOrderStatus.PENDING_FINAL_APPROVAL)) {
//            throw new UserException("该用户无权设置该工单状态");
//        }
        UserEntity engineerEntity = null;
        if (dto.getEngineerId() != null) {
            engineerEntity = userRepository.findById(dto.getEngineerId()).orElse(null);
        }
        if (engineerEntity != null) {
            workOrderEntity.setEngineer(engineerEntity);
        }
        if (dto.getDescription() != null) {
            workOrderEntity.setDescription(dto.getDescription());
        }
        WorkOrderActionEntity workOrderActionEntity = new WorkOrderActionEntity(workOrderEntity,
                workOrderEntity.getStatus(), dto.getStatus());
        workOrderActionEntity.setUser(user);
        workOrderActionEntity.setComment(dto.getComment());
        workOrderActionEntity.setDate(LocalDateTime.now());
        workOrderActionRepository.save(workOrderActionEntity);
        List<WorkOrderActionEntity> workOrderActionEntityList = workOrderEntity.getActions();
        workOrderActionEntityList.add(workOrderActionEntity);
        workOrderEntity.setActions(workOrderActionEntityList);
        workOrderEntity.setStatus(dto.getStatus());
        if (dto.getStatus() == WorkOrderStatus.PENDING_FINAL_APPROVAL) {
            for (PartListEntity partListEntity : workOrderEntity.getPartLists()) {
                addActionHistory(partListEntity, PartListStatus.PENDING_APPROVAL, user, "");
                partListEntity.setWorker(user);
                partListEntity.setPartListStatus(PartListStatus.PENDING_APPROVAL);
                partListEntity.setCreateDate(LocalDateTime.now());
                partListRepository.save(partListEntity);
            }
        } else if (dto.getStatus() == WorkOrderStatus.APPROVED) {
            for (PartListEntity partListEntity : workOrderEntity.getPartLists()) {
                addActionHistory(partListEntity, PartListStatus.APPROVED, user, "");
                partListEntity.setPartListStatus(PartListStatus.APPROVED);
                partListRepository.save(partListEntity);
            }
        }
        workOrderRepository.save(workOrderEntity);
        return workOrderEntity;
    }

}

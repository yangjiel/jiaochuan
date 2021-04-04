package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.PostWorkOrderDto;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListStatus;
import com.jiaochuan.hazakura.entity.workorder.Status;
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
    private EquipmentRepository equipmentRepository;

    @Autowired
    private PartListEquipmentRepository partListEquipmentRepository;

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
        workOrderEntity.setAddress(dto.getAddress());
        workOrderEntity.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.PENDING_FIRST_APPROVAL);
        workOrderEntity.setDescription(dto.getDescription());
        workOrderEntity.setServiceItem(dto.getServiceItem());
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
                                               Status status,
                                               PartListStatus partListStatus,
                                               String orderBy) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WorkOrderEntity> cq = cb.createQuery(WorkOrderEntity.class);

        Root<WorkOrderEntity> workOrder = cq.from(WorkOrderEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (client != null) {
            predicates.add(cb.equal(workOrder.get("client"), client));
        }
        if (worker != null) {
            predicates.add(cb.equal(workOrder.get("worker"), worker));
        }
        if (datetime != null) {
            predicates.add(cb.equal(workOrder.get("createDate"), datetime));
        }
        if (status != null) {
            predicates.add(cb.equal(workOrder.get("status"), status));
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
                        workOrder.join("client").get("userName"), cb.literal("gbk"))));
                break;
            case "nameAsc":
                cq.orderBy(cb.asc(cb.function(
                        "convertEncode",
                        String.class,
                        workOrder.join("client").get("userName"), cb.literal("gbk"))));
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
                                                       WorkOrderEntity input) throws UserException {
        WorkOrderEntity workOrderEntity = workOrderRepository.findById(input.getId()).orElse(null);
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (workOrderEntity == null) {
            throw new UserException("工单不存在！");
        }
        if (user == null) {
            throw new UserException("用户不存在！");
        }
//        if ((user.getRole() == Role.DIRECTOR_AFTER_SALES &&
//                input.getStatus() != Status.PENDING_FINAL_APPROVAL)) {
//            throw new UserException("该用户无权设置该工单状态");
//        }
        if (input.getEngineer() != null) {
            workOrderEntity.setEngineer(input.getEngineer());
        }
        workOrderEntity.setStatus(input.getStatus());
        if (input.getStatus() == Status.PENDING_FINAL_APPROVAL) {
            PartListEntity partListEntity = workOrderEntity.getPartLists().get(0);
            partListEntity.setWorker(user);
            partListEntity.setCreateDate(LocalDateTime.now());
            partListRepository.save(partListEntity);
        } else if (input.getStatus() == Status.PENDING_DISPATCH) {
            for (PartListEntity partListEntity : workOrderEntity.getPartLists()) {
                if (partListEntity.getPartListStatus() == PartListStatus.PENDING_FINALIZE) {
                    partListEntity.setPartListStatus(PartListStatus.PENDING_APPROVAL);
                    partListRepository.save(partListEntity);
                }
            }
        } else if (workOrderEntity.containAllPartListStatus(PartListStatus.READY)) {
            workOrderEntity.setStatus(Status.PROCEEDING);
        }
        workOrderRepository.save(workOrderEntity);
        return workOrderEntity;
    }

}

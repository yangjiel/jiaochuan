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

    @Autowired
    private EntityManager em;

    @Transactional
    public RequisitionsEntity createRequisitions(RequisitionsDto dto) throws AppException, UserException {
        Set<String> mandatoryFieldsSet = Set.of("creatorId", "workOrderId", "departmentId");
        Helper.checkFields(RequisitionsDto.class, dto, mandatoryFieldsSet);

        WorkOrderEntity workOrderEntity = workOrderRepository.findById(dto.getWorkOrderId()).orElse(null);
        DepartmentEntity departmentEntity = departmentRepository.findById(dto.getDepartmentId()).orElse(null);
        if (workOrderEntity == null) {
            throw new UserException(String.format("ID为%s的工单不存在！", dto.getWorkOrderId()));
        }

        UserEntity creatorEntity = userRepository.findById(dto.getCreatorId()).orElse(null);
        if (creatorEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在！", dto.getCreatorId()));
        }

        RequisitionsEntity requisitionsEntity = new RequisitionsEntity(
                creatorEntity,
                workOrderEntity,
                RequisitionsStatus.PENDING_PURCHASE,
                LocalDateTime.now());
        requisitionsEntity.setDepartment(departmentEntity);
        requisitionsEntity.setDepartment(departmentRepository.findById(dto.getDepartmentId()).orElse(null));
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

    public List<RequisitionsEntity> getRequisitions(int page,
                                                    int size,
                                                    Long creator,
                                                    Long purchaser,
                                                    Long worker,
                                                    LocalDateTime datetime,
                                                    RequisitionsStatus status,
                                                    String orderBy) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RequisitionsEntity> cq = cb.createQuery(RequisitionsEntity.class);

        Root<RequisitionsEntity> requisitionsEntityRoot = cq.from(RequisitionsEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        if (creator != null) {
            predicates.add(cb.equal(requisitionsEntityRoot.get("creator"), creator));
        }
        if (purchaser != null) {
            predicates.add(cb.equal(requisitionsEntityRoot.get("purchaser"), purchaser));
        }
        if (worker != null) {
            predicates.add(cb.equal(requisitionsEntityRoot.get("worker"), worker));
        }
        if (datetime != null) {
            predicates.add(cb.equal(requisitionsEntityRoot.get("createdDate"), datetime));
        }
        if (status != null) {
            predicates.add(cb.equal(requisitionsEntityRoot.get("status"), status));
        }

        switch (orderBy != null ? orderBy : "") {
            case "timeAsc":
                cq.orderBy(cb.asc(requisitionsEntityRoot.get("id")));
                break;
            default:
                cq.orderBy(cb.desc(requisitionsEntityRoot.get("id")));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        List<RequisitionsEntity> list = em.createQuery(cq).getResultList();
        PagedListHolder<RequisitionsEntity> pagedListHolder = new PagedListHolder<>(list);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);
        return list;
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

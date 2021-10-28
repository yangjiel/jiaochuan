package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.InspectionDto;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.*;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.DepartmentRepository;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.InspectionActionRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.InspectionRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.RequisitionsRepository;
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
public class InspectionService {
    @Autowired
    InspectionRepository inspectionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequisitionsRepository requisitionsRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    InspectionActionRepository inspectionActionRepository;

    @Autowired
    EntityManager em;

    @Transactional
    public InspectionEntity createInspection(InspectionDto dto) throws UserException, AppException {
        // Check if required fields are not empty
        //inspectionEntity.setCreatedDate(LocalDateTime.now());
        Set<String> mandatoryFieldsSet = Set.of("creatorId", "requisitionsId");
        Helper.checkFields(InspectionDto.class, dto, mandatoryFieldsSet);

        UserEntity userEntity = userRepository.findById(dto.getCreatorId()).orElse(null);
        if (userEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在！", dto.getCreatorId()));
        }

        RequisitionsEntity requisitionsEntity = requisitionsRepository.findById(dto.getRequisitionsId()).orElse(null);
        if (requisitionsEntity == null) {
            throw new UserException(String.format("ID为%s的采购单不存在！", dto.getRequisitionsId()));
        }

        InspectionStatus status = dto.getStatus() == null ? InspectionStatus.PENDING_APPROVAL : dto.getStatus();

        InspectionEntity inspectionEntity = new InspectionEntity(
                userEntity,
                requisitionsEntity,
                status,
                LocalDateTime.now());

        inspectionEntity.setDepartment(departmentRepository.findById(dto.getDepartmentId()).orElse(null));

        persistInspectionHelper(inspectionEntity, dto);

        InspectionActionEntity inspectionActionEntity = new InspectionActionEntity(
                inspectionEntity,
                status,
                status);
        inspectionActionEntity.setUser(userEntity);
        inspectionActionEntity.setDate(LocalDateTime.now());
        inspectionActionRepository.save(inspectionActionEntity);
        List<InspectionActionEntity> inspectionEntityArrayList = new ArrayList<>();
        inspectionEntityArrayList.add(inspectionActionEntity);
        inspectionEntity.setActions(inspectionEntityArrayList);

        inspectionRepository.save(inspectionEntity);
        return inspectionEntity;
    }

    public List<InspectionEntity> getInspections(int page,
                                                 int size,
                                                 Long creator,
                                                 Long requisitionsEntity,
                                                 LocalDateTime dateTime,
                                                 InspectionStatus inspectionStatus,
                                                 String orderBy) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<InspectionEntity> cq = cb.createQuery(InspectionEntity.class);
        Root<InspectionEntity> inspectionEntityRoot = cq.from(InspectionEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (creator != null) {
            predicates.add(cb.equal(inspectionEntityRoot.get("creator"), creator));
        }
        if (requisitionsEntity != null) {
            predicates.add(cb.equal(inspectionEntityRoot.get("requisitions"), requisitionsEntity));
        }
        if (dateTime != null) {
            predicates.add(cb.equal(inspectionEntityRoot.get("createDate"), dateTime));
        }
        if (inspectionStatus != null) {
            predicates.add(cb.equal(inspectionEntityRoot.get("status"), inspectionStatus));
        }

        switch (orderBy != null ? orderBy : "") {
            case "timeAsc":
                cq.orderBy(cb.asc(inspectionEntityRoot.get("id")));
                break;
            default:
                cq.orderBy(cb.desc(inspectionEntityRoot.get("id")));
                break;
        }
        cq.where(predicates.toArray(new Predicate[0]));
        List<InspectionEntity> list = em.createQuery(cq).getResultList();
        PagedListHolder<InspectionEntity> pagedList = new PagedListHolder<>(list);
        pagedList.setPageSize(size);
        pagedList.setPage(page);
        return pagedList.getPageList();
    }

    @Transactional
    public InspectionEntity updateInspection(Long userId,
                                             InspectionDto dto) throws UserException, AppException {
        InspectionEntity inspectionEntity = inspectionRepository.findById(dto.getInspectionId()).orElse(null);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (inspectionEntity == null) {
            throw new UserException(String.format("ID为%s的检验单不存在！", dto.getInspectionId()));
        }

        if (userEntity == null) {
            throw new UserException("用户不存在！");
        }

        if (dto.getStatus() != null) {
            InspectionActionEntity inspectionActionEntity = new InspectionActionEntity(
                    inspectionEntity,
                    inspectionEntity.getStatus(),
                    dto.getStatus());
            inspectionActionEntity.setUser(userEntity);
            inspectionActionEntity.setDate(LocalDateTime.now());
            inspectionActionRepository.save(inspectionActionEntity);
            List<InspectionActionEntity> inspectionActionEntityList = inspectionEntity.getActions();
            inspectionActionEntityList.add(inspectionActionEntity);
            inspectionEntity.setActions(inspectionActionEntityList);
        }

        persistInspectionHelper(inspectionEntity, dto);

        inspectionRepository.save(inspectionEntity);
        return inspectionEntity;
    }

    private void persistInspectionHelper(InspectionEntity inspectionEntity, InspectionDto dto) {
        inspectionEntity.setProductName(dto.getProductName());
        inspectionEntity.setModel(dto.getModel());
        inspectionEntity.setSerialNumber(dto.getSerialNumber());
        inspectionEntity.setQuantity(dto.getQuantity());
        inspectionEntity.setUnit(dto.getUnit());
        inspectionEntity.setManufacturer(dto.getManufacturer());
        inspectionEntity.setSizeFit(dto.getSizeFit());
        inspectionEntity.setQualityCertificate(dto.getQualityCertificate());
        inspectionEntity.setExterior(dto.getExterior());
        inspectionEntity.setLogo(dto.getLogo());
        inspectionEntity.setPackaging(dto.getPackaging());
        inspectionEntity.setNote(dto.getNote());
        inspectionEntity.setSamplingMethod(dto.getSamplingMethod());
    }
}

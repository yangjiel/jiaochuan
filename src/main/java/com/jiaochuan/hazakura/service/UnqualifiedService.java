package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.workorder.UnqualifiedDto;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.*;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.DepartmentRepository;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.InspectionRepository;
import com.jiaochuan.hazakura.jpa.WorkOrder.UnqualifiedRepository;
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
public class UnqualifiedService {
    @Autowired
    UnqualifiedRepository unqualifiedRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InspectionRepository inspectionRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EntityManager em;

    @Transactional
    public UnqualifiedEntity createUnqualified(UnqualifiedDto dto) throws UserException, AppException {
        // Check if required fields are not empty
        Set<String> mandatoryFieldsSet = Set.of("creatorId", "inspectionId");
        Helper.checkFields(UnqualifiedDto.class, dto, mandatoryFieldsSet);

        UserEntity userEntity = userRepository.findById(dto.getCreatorId()).orElse(null);
        if (userEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在！", dto.getCreatorId()));
        }

        InspectionEntity inspectionActionEntity = inspectionRepository.findById(dto.getInspectionId()).orElse(null);
        if (inspectionActionEntity == null) {
            throw new UserException(String.format("ID为%s的检验单不存在！", dto.getInspectionId()));
        }

        UnqualifiedEntity unqualifiedEntity = new UnqualifiedEntity(
                inspectionActionEntity,
                userEntity,
                LocalDateTime.now());

        persistUnqualifiedHelper(unqualifiedEntity, dto);

        unqualifiedRepository.save(unqualifiedEntity);
        return unqualifiedEntity;
    }

    public List<UnqualifiedEntity> getUnqualified(int page,
                                                 int size,
                                                 Long creator,
                                                 Long inspectionEntity,
                                                 LocalDateTime dateTime,
                                                 String orderBy) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UnqualifiedEntity> cq = cb.createQuery(UnqualifiedEntity.class);
        Root<UnqualifiedEntity> inspectionEntityRoot = cq.from(UnqualifiedEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (creator != null) {
            predicates.add(cb.equal(inspectionEntityRoot.get("creator"), creator));
        }
        if (inspectionEntity != null) {
            predicates.add(cb.equal(inspectionEntityRoot.get("inspection"), inspectionEntityRoot));
        }
        if (dateTime != null) {
            predicates.add(cb.equal(inspectionEntityRoot.get("createDate"), dateTime));
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
        List<UnqualifiedEntity> list = em.createQuery(cq).getResultList();
        PagedListHolder<UnqualifiedEntity> pagedList = new PagedListHolder<>(list);
        pagedList.setPageSize(size);
        pagedList.setPage(page);
        return pagedList.getPageList();
    }

    @Transactional
    public UnqualifiedEntity updateUnqualified(UnqualifiedDto dto) throws UserException, AppException {
        UnqualifiedEntity unqualifiedEntity = unqualifiedRepository.findById(dto.getUnqualifiedId()).orElse(null);
        if (unqualifiedEntity == null) {
            throw new UserException(String.format("ID为%s的不合格单不存在！", dto.getUnqualifiedId()));
        }

        persistUnqualifiedHelper(unqualifiedEntity, dto);

        unqualifiedRepository.save(unqualifiedEntity);
        return unqualifiedEntity;
    }

    private void persistUnqualifiedHelper(UnqualifiedEntity unqualifiedEntity, UnqualifiedDto dto) {
        UserEntity responsiblePerson = userRepository.findById(dto.getResponsiblePersonId()).orElse(null);
        UserEntity inspector = userRepository.findById(dto.getReworkInspectorId()).orElse(null);
        UserEntity operator = userRepository.findById(dto.getOperatorId()).orElse(null);

        if (unqualifiedEntity.getDepartment() == null && dto.getDepartmentId() != null) {
            unqualifiedEntity.setDepartment(departmentRepository.findById(dto.getDepartmentId()).orElse(null));
        }
        if (unqualifiedEntity.getDepartment() == null && responsiblePerson != null) {
            unqualifiedEntity.setResponsiblePerson(responsiblePerson);
        }
        if (unqualifiedEntity.getDepartment() == null && inspector != null) {
            unqualifiedEntity.setReworkInspector(inspector);
        }
        if (unqualifiedEntity.getDepartment() == null && operator != null) {
            unqualifiedEntity.setOperator(operator);
        }
        unqualifiedEntity.setUnqualifiedLevel(dto.getUnqualifiedLevel());
        unqualifiedEntity.setInfluence(dto.getInfluence());
        unqualifiedEntity.setAcceptanceDetails(dto.getAcceptanceDetails());
        unqualifiedEntity.setStandardOfAcceptance(dto.getStandardOfAcceptance());
        unqualifiedEntity.setReworkQuantity(dto.getReworkQuantity());
        unqualifiedEntity.setReworkDetails(dto.getReworkDetails());
        unqualifiedEntity.setReworkInspection(dto.getReworkInspection());
        unqualifiedEntity.setNoReworkQuantity(dto.getNoReworkQuantity());
        unqualifiedEntity.setReworkAcceptedQuantity(dto.getReworkAcceptedQuantity());
        unqualifiedEntity.setNoReworkAcceptedQuantity(dto.getNoReworkAcceptedQuantity());
        unqualifiedEntity.setRejectedQuantity(dto.getRejectedQuantity());
    }
}

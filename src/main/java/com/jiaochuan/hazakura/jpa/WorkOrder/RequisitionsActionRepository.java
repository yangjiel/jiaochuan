package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.workorder.RequisitionsActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisitionsActionRepository extends JpaRepository<RequisitionsActionEntity, Long> {
    public RequisitionsActionEntity findByRequisitionsId(String RequisitionsId);
}

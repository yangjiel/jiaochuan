package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.workorder.RequisitionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisitionsRepository extends JpaRepository<RequisitionsEntity, Long> {
    public RequisitionsEntity findByCreatorId(String creatorId);
}

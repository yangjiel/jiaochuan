package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.workorder.PartListActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartListActionRepository extends JpaRepository<PartListActionEntity, Long> {
    public PartListActionEntity findByPartListId(String workOrderId);
}

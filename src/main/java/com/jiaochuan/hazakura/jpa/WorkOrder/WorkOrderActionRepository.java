package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.workorder.WorkOrderActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderActionRepository extends JpaRepository<WorkOrderActionEntity, Long> {
    public WorkOrderActionEntity findByWorkOrderId(String workOrderId);
}

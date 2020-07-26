package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartListRepository extends JpaRepository<PartListEntity, Long> {
    public PartListEntity findByWorkOrderId(String workOrderId);
}

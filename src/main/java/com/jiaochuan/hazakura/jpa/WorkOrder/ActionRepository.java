package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.workorder.ActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, Long> {
    public ActionEntity findByWorkOrderId(String workOrderId);
}

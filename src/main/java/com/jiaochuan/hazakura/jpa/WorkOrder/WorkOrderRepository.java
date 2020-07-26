package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.user.CustomerEntity;
import com.jiaochuan.hazakura.entity.workorder.WorkOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkOrderRepository extends JpaRepository<WorkOrderEntity, Long> {
    public WorkOrderEntity findByCustomer(CustomerEntity customer);
}

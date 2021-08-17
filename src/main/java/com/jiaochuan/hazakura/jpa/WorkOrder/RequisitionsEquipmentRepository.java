package com.jiaochuan.hazakura.jpa.WorkOrder;

import com.jiaochuan.hazakura.entity.workorder.RequisitionsEquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequisitionsEquipmentRepository extends JpaRepository<RequisitionsEquipmentEntity, Long> {
}

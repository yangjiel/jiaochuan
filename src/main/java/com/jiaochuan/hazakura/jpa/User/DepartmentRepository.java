package com.jiaochuan.hazakura.jpa.User;

import com.jiaochuan.hazakura.entity.user.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    public DepartmentEntity findByDepartment(String department);
}

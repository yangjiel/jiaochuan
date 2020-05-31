package com.jiaochuan.hazakura.jpa;

import com.jiaochuan.hazakura.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

}

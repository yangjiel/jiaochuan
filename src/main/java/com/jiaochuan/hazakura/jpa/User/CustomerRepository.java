package com.jiaochuan.hazakura.jpa.User;

import com.jiaochuan.hazakura.entity.user.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    public CustomerEntity findByContactName(String contactName);
}

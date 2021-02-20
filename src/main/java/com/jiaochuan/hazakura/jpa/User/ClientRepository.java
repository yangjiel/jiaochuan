package com.jiaochuan.hazakura.jpa.User;

import com.jiaochuan.hazakura.entity.user.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public ClientEntity findByContactName(String contactName);
    public ClientEntity findByUserName(String userName);
    public ClientEntity findByCell(String cell);
}

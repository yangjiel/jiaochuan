package com.jiaochuan.hazakura.jpa.User;

import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByUsername(String username);

    public UserEntity findByCell(String cell);

    public UserEntity findByRole(Role role);
}

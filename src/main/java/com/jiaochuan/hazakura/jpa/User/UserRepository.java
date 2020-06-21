package com.jiaochuan.hazakura.jpa.User;

import com.jiaochuan.hazakura.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}

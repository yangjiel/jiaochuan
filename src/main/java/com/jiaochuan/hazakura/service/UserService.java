package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser() {
        userRepository.save(new UserEntity("peter", "test", "test", "test", Role.STAFF_CUSTOMER_SERVICE, "test", "test"));
    }
}

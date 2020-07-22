package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import liquibase.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(UserEntity userEntity) throws AppException, UserException {
        // Check if required fields are not empty
        try {
            for (Field field : userEntity.getClass().getDeclaredFields()) {
                if (field.trySetAccessible()) {
                    if (field.getType() == String.class && StringUtils.isBlank((String) field.get(userEntity))) {
                        throw new UserException("必填项" + field.getName() + "不能为空。");
                    } else if (field.get(userEntity) == null) {
                        throw new UserException("必填项" + field.getName() + "不能为空。");
                    }
                } else {
                    throw new AppException("创建用户时无法取得UserEntity的反射访问权限，其成员变量无法通过反射访问。");
                }
            }
        } catch(IllegalAccessException e) {
            throw new AppException("创建用户时无法取得UserEntity的反射访问权限，其成员变量无法通过反射访问。");
        }


        // Check if username has been taken
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            throw new UserException("用户名已被使用。");
        }

        // Check if password is within constraint
        String password = userEntity.getPassword();
        if (StringUtils.isAlphanumericSpace(password)) {
            throw new UserException("密码中存在特殊字符，请检查输入。");
        }
        if (password.length() < 8) {
            throw new UserException("密码长度不能小于8位字符。");
        }
        if (password.length() > 16) {
            throw new UserException("密码长度不能大于16位字符。");
        }

        // Check format for cell phone and email
        if (userEntity.getCell().length() > 11) {
            throw new UserException("手机号码长度不能多于11位。");
        }
        if (!userEntity.getEmail().contains("@")) {
            throw new UserException("电子邮箱格式不正确。");
        }

        userRepository.save(userEntity);
    }
}

package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity createUser(UserEntity userEntity) throws AppException, UserException {
        // Check if required fields are not empty
        Helper.checkFields(UserEntity.class, userEntity);

        if (userEntity.getUsername().length() > 16 || userEntity.getFirstName().length() > 16) {
            throw new UserException("用户名长度不能大于16个字符！");
        }

        // Check if username has been taken
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            throw new UserException("用户名已被使用。");
        }

        // Check if password is within constraint
        String password = userEntity.getPassword();
        if (!StringUtils.isAlphanumericSpace(password)) {
            throw new UserException("密码中存在特殊字符，请检查输入。");
        }
        if (password.length() < 8) {
            throw new UserException("密码长度不能小于8位字符。");
        }
        if (password.length() > 16) {
            throw new UserException("密码长度不能大于16位字符。");
        }
        if (userEntity.getLastName().length() > 4) {
            throw new UserException("用户姓氏长度不能大于4个字符！");
        }

        // Hash password
        userEntity.setPassword(passwordEncoder.encode(password));

        // Check format for cell phone and email
        if (userEntity.getCell().length() != 11) {
            throw new UserException("手机号码长度不能多于或少于11位。");
        }
        if (!userEntity.getEmail().contains("@")) {
            throw new UserException("电子邮箱格式不正确。");
        }
        if (userEntity.getEmail().length() > 64) {
            throw new UserException("电子邮箱长度不能大于64个字符！");
        }

        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("用户名不存在。");
        }

        return userEntity;
    }

    public List<UserEntity> getUsers(int page, int size) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

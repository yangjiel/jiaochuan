package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.user.UpdateUserDto;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jiaochuan.hazakura.service.Helper.*;

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

        checkUsername(userEntity.getUsername());
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            throw new UserException("用户名已被使用！");
        }
        String password = userEntity.getPassword();
        checkPassword(password);
        if (userEntity.getLastName().length() > 4) {
            throw new UserException("用户姓氏长度不能大于4个字符！");
        }

        // Hash password
        userEntity.setPassword(passwordEncoder.encode(password));
        checkCell(userEntity.getCell());
        checkEmail(userEntity.getEmail());

        userRepository.save(userEntity);
        return userEntity;
    }

    @Transactional
    public UserEntity updateUser(Long userId,
                                 UpdateUserDto dto) throws UserException {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserException("用户不存在！");
        }
        if (dto.getUsername() != null) {
            checkUsername(dto.getUsername());
        }
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new UserException("用户名已被使用！");
        }
        String password = dto.getPassword();
        if (password != null) {
            checkPassword(password);
            // Hash password
            if (passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(password));
            }
        }
        if (dto.getCell() != null) {
            checkCell(dto.getCell());
        }
        if (dto.getEmail() != null) {
            checkEmail(dto.getEmail());
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        return userEntity;
    }

    public List<UserEntity> getUsers(int page, int size) throws UserException {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0！");
        }
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

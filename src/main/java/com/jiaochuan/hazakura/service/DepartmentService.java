package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.user.DepartmentEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.DepartmentRepository;
import com.jiaochuan.hazakura.jpa.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DepartmentEntity createDepartment(String department, String leaderId) throws UserException {
        if (department.length() > 32) {
            throw new UserException("部门名长度不能大于32个字符！");
        }
        UserEntity userEntity = userRepository.findById(Long.parseLong(leaderId)).orElse(null);
        if (userEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在！", leaderId));
        }
        DepartmentEntity departmentEntity = new DepartmentEntity(department, userEntity);
        departmentRepository.save(departmentEntity);
        return departmentEntity;
    }
}

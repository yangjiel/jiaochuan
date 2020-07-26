package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.user.CustomerEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.User.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void createCustomer(CustomerEntity customerEntity) throws AppException, Exception {
        // Check if required fields are not empty
        try {
            for (Field field : customerEntity.getClass().getDeclaredFields()) {
                if (!field.trySetAccessible()) {
                    throw new AppException("创建用户时无法取得CustomerEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(customerEntity))) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                } else if (field.get(customerEntity) == null) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch(IllegalAccessException e) {
            throw new AppException("创建用户时无法取得CustomerEntity的反射访问权限，其成员变量无法通过反射访问。");
        }


//        // Check if username has been taken
//        if (customerRepository.findByContactName(customerEntity.getContactName()) != null) {
//            throw new Exception("用户名已被使用。");
//        }

        // Check format for cell phone and email
        if (customerEntity.getCell().length() != 11) {
            throw new Exception("手机号码长度不能多于或少于11位。");
        }
        if (!customerEntity.getEmail().contains("@")) {
            throw new Exception("电子邮箱格式不正确。");
        }

        customerRepository.save(customerEntity);
    }
}

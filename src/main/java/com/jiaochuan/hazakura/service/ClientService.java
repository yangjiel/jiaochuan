package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.User.ClientRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public void createClient(ClientEntity clientEntity) throws AppException, Exception {
        // Check if required fields are not empty
        try {
            for (Field field : clientEntity.getClass().getDeclaredFields()) {
                if (!field.trySetAccessible()) {
                    throw new AppException("创建用户时无法取得ClientEntity的反射访问权限，其成员变量无法通过反射访问。");
                }

                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(clientEntity))) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                } else if (field.get(clientEntity) == null) {
                    throw new Exception("必填项" + field.getName() + "不能为空。");
                }
            }
        } catch(IllegalAccessException e) {
            throw new AppException("创建用户时无法取得ClientEntity的反射访问权限，其成员变量无法通过反射访问。");
        }


//        // Check if username has been taken
//        if (clientRepository.findByContactName(clientEntity.getContactName()) != null) {
//            throw new Exception("用户名已被使用。");
//        }

        // Check format for cell phone and email
        if (clientEntity.getCell().length() != 11) {
            throw new Exception("手机号码长度不能多于或少于11位。");
        }
        if (!clientEntity.getEmail().contains("@")) {
            throw new Exception("电子邮箱格式不正确。");
        }

        clientRepository.save(clientEntity);
    }

    public List<ClientEntity> getClients(int page, int size) throws Exception {
        if (page < 0 || size < 0) {
            throw new Exception("分页设置不能小于0。");
        }
        return clientRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

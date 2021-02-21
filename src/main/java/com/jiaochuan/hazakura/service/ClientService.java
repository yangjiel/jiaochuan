package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.jpa.User.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public void createClient(ClientEntity clientEntity) throws AppException, Exception {
        // Check if required fields are not empty
        Helper.checkFields(ClientEntity.class, clientEntity);


//        // Check if username has been taken
//        if (clientRepository.findByContactName(clientEntity.getContactName()) != null) {
//            throw new Exception("用户名已被使用。");
//        }

        // Check format for cell phone and email
        if (clientEntity.getCell().length() != 11) {
            throw new Exception("手机号码长度不能多于或少于11位。");
        }
        if (!clientEntity.getEmail().equals("") && !clientEntity.getEmail().contains("@")) {
            throw new Exception("电子邮箱格式不正确。");
        }

        clientRepository.save(clientEntity);
    }

    public boolean checkClientName(String username) {
        return clientRepository.findByUserName(username) != null;
    }

    public boolean checkClientCell(String cell) {
        return clientRepository.findByCell(cell) != null;
    }

    public List<ClientEntity> getClients(int page, int size) throws Exception {
        if (page < 0 || size < 0) {
            throw new Exception("分页设置不能小于0。");
        }
        return clientRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

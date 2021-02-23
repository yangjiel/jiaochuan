package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.api.user.PatchClientDto;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public void createClient(ClientEntity clientEntity) throws AppException, UserException {
        // Check if required fields are not empty
        Helper.checkFields(ClientEntity.class, clientEntity);

        // Check format for cell phone and email
        if (clientEntity.getUserName().length() > 20) {
            throw new UserException("用户名长度不能大于20个字符！");
        }
        if (clientEntity.getContactName().length() > 20) {
            throw new UserException("联系名长度不能大于20个字符！");
        }
        if (clientEntity.getCell().length() != 11) {
            throw new UserException("手机号码长度不能多于或少于11位。");
        }
        if (!clientEntity.getEmail().equals("") && !clientEntity.getEmail().contains("@")) {
            throw new UserException("电子邮箱格式不正确。");
        }
        if (clientEntity.getEmail().length() > 64) {
            throw new UserException("电子邮箱长度不能大于64个字符！");
        }
        if (clientEntity.getCompanyAddress().length() > 100) {
            throw new UserException("公司地址长度不能大于100个字符！");
        }
        if (clientEntity.getNotes().length() > 100) {
            throw new UserException("用户备注不能大于100个字符！");
        }

        if (clientRepository.findByUserName(clientEntity.getUserName()) != null) {
            throw new UserException("该客户名已存在！");
        } else if (clientRepository.findByCell(clientEntity.getCell()) != null) {
            throw new UserException("该客户手机号码已存在！");
        }

        clientRepository.save(clientEntity);
    }

    public void patchClient(PatchClientDto dto) throws AppException, UserException{
        Set<String> mandatoryFieldsSet = Set.of("clientId", "userName", "contactName", "cell", "companyAddress");
        Helper.checkFields(PatchClientDto.class, dto, mandatoryFieldsSet);
        ClientEntity clientEntity = clientRepository.findById(dto.getClientId()).orElse(null);
        if (clientEntity == null) {
            throw new UserException(String.format("ID为%s的用户不存在。", dto.getClientId()));
        }
        clientEntity.setUserName(dto.getUserName());
        clientEntity.setContactName(dto.getContactName());
        clientEntity.setCell(dto.getCell());
        clientEntity.setEmail(dto.getEmail());
        clientEntity.setCompanyAddress(dto.getCompanyAddress());
        clientEntity.setNotes(dto.getNotes());
        clientRepository.save(clientEntity);
    }

    public List<ClientEntity> getClients(int page, int size) throws Exception {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        return clientRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}

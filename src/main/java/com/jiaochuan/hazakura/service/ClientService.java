package com.jiaochuan.hazakura.service;

import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.jpa.User.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityManager em;

    private void checkClient(ClientEntity clientEntity) throws UserException {
        // Check format for cell phone and email
        if (clientEntity.getUserName().length() > 20) {
            throw new UserException("客户名长度不能大于20个字符！");
        }
        if (clientEntity.getContactName().length() > 20) {
            throw new UserException("客户联系姓名长度不能大于20个字符！");
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
        if (clientEntity.getNotes().length() > 256) {
            throw new UserException("客户备注不能大于256个字符！");
        }
    }

    @Transactional
    public void createClient(ClientEntity clientEntity) throws AppException, UserException {
        // Check if required fields are not empty
        Helper.checkFields(ClientEntity.class, clientEntity);
        clientEntity.setId(null);

        checkClient(clientEntity);

        if (clientRepository.findByUserName(clientEntity.getUserName()) != null) {
            throw new UserException("该客户名已存在！");
        } else if (clientRepository.findByCell(clientEntity.getCell()) != null) {
            throw new UserException("该客户手机号码已存在！");
        }

        clientRepository.save(clientEntity);
    }

    public void updateClient(ClientEntity clientEntity) throws AppException, UserException {
        Helper.checkFields(ClientEntity.class, clientEntity, Set.of("id"));
        ClientEntity check = clientRepository.findById(clientEntity.getId()).orElse(null);
        if (check == null) {
            throw new UserException(String.format("ID为%s的客户不存在。", clientEntity.getId()));
        }

        checkClient(clientEntity);

        check = clientRepository.findByUserName(clientEntity.getUserName());
        if (check != null && !check.getId().equals(clientEntity.getId())) {
            throw new UserException("该客户名已存在");
        }
        check = clientRepository.findByCell(clientEntity.getCell());
        if (check != null && !check.getId().equals(clientEntity.getId())) {
            throw new UserException("该客户手机号码已存在");
        }
        clientRepository.save(clientEntity);
    }

    public List<ClientEntity> getClients(int page, int size, String orderBy) throws Exception {
        if (page < 0 || size < 0) {
            throw new UserException("分页设置不能小于0。");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ClientEntity> cq = cb.createQuery(ClientEntity.class);

        Root<ClientEntity> client = cq.from(ClientEntity.class);

        if (orderBy != null && orderBy.equals("nameDesc")) {
            cq.orderBy(cb.desc(cb.function("convertEncode",
                    String.class, client.get("userName"),
                    cb.literal("gbk"))));
        } else {
            cq.orderBy(cb.asc(cb.function("convertEncode",
                    String.class, client.get("userName"),
                    cb.literal("gbk"))));
        }

        List<ClientEntity> list = em.createQuery(cq).getResultList();
        PagedListHolder<ClientEntity> pagedList = new PagedListHolder<>(list);
        pagedList.setPageSize(size);
        pagedList.setPage(page);
        return pagedList.getPageList();
    }
}

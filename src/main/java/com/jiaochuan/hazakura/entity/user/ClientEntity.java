package com.jiaochuan.hazakura.entity.user;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "client")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity extends AbstractEntity {
    @Column(name = "user_name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String userName;

    @Column(name = "contact_name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String contactName;

    @Column(name = "cell", columnDefinition = "CHAR(11)", nullable = false)
    private String cell;

    @Column(name = "email", columnDefinition = "VARCHAR(64)")
    private String email;

    @Column(name = "company_address", columnDefinition = "VARCHAR(100)", nullable = false)
    private String companyAddress;

    @Column(name = "notes", columnDefinition = "VARCHAR(256)")
    private String notes;
}

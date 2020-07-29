package com.jiaochuan.hazakura.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name="customer")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends AbstractEntity {
    @Column(name = "user_name", columnDefinition = "VARCHAR(16)", nullable = false)
    private String userName;

    @Column(name = "contact_name", columnDefinition = "CHAR(60)", nullable = false)
    private String contactName;

    @Column(name = "cell", columnDefinition = "CHAR(11)", nullable = false)
    private String cell;

    @Column(name = "email", columnDefinition = "VARCHAR(64)")
    private String email;

    @Column(name = "company_address", columnDefinition = "NVARCHAR(16)", nullable = false)
    private String companyAddress;
}

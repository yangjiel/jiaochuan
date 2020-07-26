package com.jiaochuan.hazakura.entity.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name="customer")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends AbstractEntity {
    @Column(name = "user_name", columnDefinition = "VARCHAR(16)", nullable = false)
    public String username;

    @Column(name = "contact_name", columnDefinition = "CHAR(60)", nullable = false)
    public String password;

    @Column(name = "cell", columnDefinition = "CHAR(11)", nullable = false)
    public String cell;

    @Column(name = "email", columnDefinition = "VARCHAR(64)")
    public String email;

    @Column(name = "company_address", columnDefinition = "NVARCHAR(16)", nullable = false)
    public String company_address;
}

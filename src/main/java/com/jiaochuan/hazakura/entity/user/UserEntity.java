package com.jiaochuan.hazakura.entity.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name="user")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends AbstractEntity implements UserDetails {
    @Column(name = "username", columnDefinition = "VARCHAR(16)", nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", columnDefinition = "CHAR(60)", nullable = false)
    private String password;

    @Column(name = "first_name", columnDefinition = "NVARCHAR(4)", nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "NVARCHAR(16)", nullable = false)
    private String lastName;

    @Column(name = "role", columnDefinition = "VARCHAR(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "cell", columnDefinition = "CHAR(11)", nullable = false)
    private String cell;

    @Column(name = "email", columnDefinition = "VARCHAR(64)", nullable = false)
    private String email;

    @Column(name = "birthday", columnDefinition = "DATE", nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(role.name()));
        return grantedAuthorityList;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}

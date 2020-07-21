package com.jiaochuan.hazakura.entity.user;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name="user")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends AbstractEntity implements UserDetails {
    @Column(name = "username", columnDefinition = "VARCHAR(16)", nullable = false)
    public String username;

    @Column(name = "password", columnDefinition = "CHAR(60)", nullable = false)
    public String password;

    @Column(name = "first_name", columnDefinition = "NVARCHAR(4)", nullable = false)
    public String firstName;

    @Column(name = "last_name", columnDefinition = "NVARCHAR(16)", nullable = false)
    public String lastName;

    @Column(name = "role", columnDefinition = "VARCHAR(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    public Role role;

    @Column(name = "cell", columnDefinition = "CHAR(11)", nullable = false)
    public String cell;

    @Column(name = "email", columnDefinition = "VARCHAR(64)", nullable = false)
    public String email;

    @Column(name = "birthday", columnDefinition = "DATE", nullable = false)
    public Instance birthday;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("USER"));
        return grantedAuthorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

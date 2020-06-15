package com.jiaochuan.hazakura.entity.User;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name="auth_user")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends AbstractEntity {
    @Column(name = "username", columnDefinition = "VARCHAR(16)", nullable = false)
    public String username;

    @Column(name = "password", columnDefinition = "CHAR(60)", nullable = false)
    public String password;

    @Column(name = "first_name", columnDefinition = "NVARCHAR(4)", nullable = false)
    public String first_name;

    @Column(name = "last_name", columnDefinition = "NVARCHAR(16)", nullable = false)
    public String last_name;

    @Column(name = "role", columnDefinition = "VARCHAR(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    public Role role;

    @Column(name = "cell", columnDefinition = "CHAR(11)", nullable = false)
    public String cell;

    @Column(name = "email", columnDefinition = "VARCHAR(64)", nullable = false)
    public String email;
}

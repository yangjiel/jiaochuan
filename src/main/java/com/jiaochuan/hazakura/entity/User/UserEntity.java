package com.jiaochuan.hazakura.entity.User;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Table(name="auth_user")
@Entity
@Data
@RequiredArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "id", columnDefinition = "MEDIUMINT")
    public Integer id;

    @Column(name = "username", length = 16)
    @NonNull
    public String username;

    @Column(name = "password", columnDefinition = "CHAR(60)")
    @NonNull
    public String password;

    @Column(name = "first_name", columnDefinition = "NVARCHAR(4)")
    @NonNull
    public String first_name;

    @Column(name = "last_name", columnDefinition = "NVARCHAR(16)")
    @NonNull
    public String last_name;

    @Column(name = "role", columnDefinition = "TINYINT")
    @NonNull
    public Integer role;

    @Column(name = "cell", length = 11)
    @NonNull
    public String cell;

    @Column(name = "email", length = 64)
    @NonNull
    public String email;
}

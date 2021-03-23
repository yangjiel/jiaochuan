package com.jiaochuan.hazakura.api.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {
    private String username;
    private String oldPassword;
    private String password;
    private String cell;
    private String email;
}

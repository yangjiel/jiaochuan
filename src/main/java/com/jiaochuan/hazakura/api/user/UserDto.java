package com.jiaochuan.hazakura.api.user;

import com.jiaochuan.hazakura.entity.user.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Role role;
    private String cell;
    private LocalDate birthday;
}

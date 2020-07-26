package com.jiaochuan.hazakura.api.user;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String status;
    private UserDto user;
}

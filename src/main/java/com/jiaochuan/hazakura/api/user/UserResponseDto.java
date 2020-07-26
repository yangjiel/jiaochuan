package com.jiaochuan.hazakura.api.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDto {
    String status;
    List<UserDto> users;
}

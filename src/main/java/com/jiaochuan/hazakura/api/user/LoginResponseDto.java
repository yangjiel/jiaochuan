package com.jiaochuan.hazakura.api.user;

import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String status;
    private UserEntity user;
}

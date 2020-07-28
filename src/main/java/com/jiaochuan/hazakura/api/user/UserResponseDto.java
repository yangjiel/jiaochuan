package com.jiaochuan.hazakura.api.user;

import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponseDto {
    String status;
    List<UserEntity> users;
}

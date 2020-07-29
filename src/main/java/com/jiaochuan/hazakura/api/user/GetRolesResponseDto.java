package com.jiaochuan.hazakura.api.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetRolesResponseDto {
    String roleId;
    String roleName;
}

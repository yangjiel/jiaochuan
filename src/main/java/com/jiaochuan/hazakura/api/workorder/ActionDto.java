package com.jiaochuan.hazakura.api.workorder;

import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.ActionType;
import lombok.Data;

import java.time.Instant;

@Data
public class ActionDto {
    private UserEntity actionUser;
    private ActionType actionType;
    private Boolean status;
    private Instant date;
}

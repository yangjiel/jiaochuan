package com.jiaochuan.hazakura.api.workorder;

import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class PartListDto {
    private UserEntity partListCreator;
    private String usage;
    private List<EquipmentDto> equipments;
}

package com.jiaochuan.hazakura.api.workorder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetTypesResponseDto {
    String typeId;
    String typeName;
}

package com.jiaochuan.hazakura.api.user;

import lombok.Data;

@Data
public class PatchClientDto {
    private Long clientId;
    private String userName;
    private String contactName;
    private String cell;
    private String email;
    private String companyAddress;
    private String notes;
}

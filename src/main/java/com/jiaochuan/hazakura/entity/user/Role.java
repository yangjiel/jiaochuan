package com.jiaochuan.hazakura.entity.user;

public enum Role {
    STAFF_CLIENT_SERVICE("客服人员"),
    STAFF_PROCUREMENT("采购人员"),
    STAFF_QUALITY_CONTROL("质检人员"),
    STAFF_INVENTORY("库管人员"),
    STAFF_TECHNICAL("技术人员"),

    MANAGER_AFTER_SALES("售后经理"),
    MANAGER_PROCEREMENT("采购经理"),
    MANAGER_QUALITY_CONTROL("质检经理"),

    DIRECTOR_AFTER_SALES("售后主任"),
    DIRECTOR_TECHNICAL("技术总监"),

    ENGINEER_AFTER_SALES("售后工程师");


    public final String roleDescription;

    private Role(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}

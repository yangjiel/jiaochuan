package com.jiaochuan.hazakura.entity.user;

public enum Role {
    STAFF_CLIENT_SERVICE("客服人员"),
    STAFF_PROCUREMENT("采购人员"),
    STAFF_QUALITY_CONTROL("质检人员"),
    STAFF_INVENTORY("库管人员"),
    STAFF_TECHNICAL("技术人员"),

    MANAGER_AFTER_SALES("售后经理"),
    MANAGER_PROCUREMENT("采购经理"),
    MANAGER_QUALITY_CONTROL("质检经理"),

    DIRECTOR_AFTER_SALES("售后主任"),
    DIRECTOR_TECHNICAL("技术总监"),

    ENGINEER_AFTER_SALES("售后工程师"),

    VICE_PRESIDENT("副总裁");

    public static class Constants {
        private Constants() {}
        public static final String STAFF_CLIENT_SERVICE = "STAFF_CLIENT_SERVICE";
        public static final String STAFF_PROCUREMENT = "STAFF_PROCUREMENT";
        public static final String STAFF_QUALITY_CONTROL = "STAFF_QUALITY_CONTROL";
        public static final String STAFF_INVENTORY = "STAFF_INVENTORY";
        public static final String STAFF_TECHNICAL = "STAFF_TECHNICAL";

        public static final String MANAGER_AFTER_SALES = "MANAGER_AFTER_SALES";
        public static final String MANAGER_PROCUREMENT = "MANAGER_PROCUREMENT";
        public static final String MANAGER_QUALITY_CONTROL = "MANAGER_QUALITY_CONTROL";

        public static final String DIRECTOR_AFTER_SALES = "DIRECTOR_AFTER_SALES";
        public static final String DIRECTOR_TECHNICAL = "DIRECTOR_TECHNICAL";

        public static final String ENGINEER_AFTER_SALES = "ENGINEER_AFTER_SALES";

        public static final String VICE_PRESIDENT = "VICE_PRESIDENT";
    }


    public final String roleDescription;

    private Role(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}

package com.jiaochuan.hazakura.entity.workorder;

public enum InspectionType {
    PERIPHERAL("外协件", (byte)0),
    FINISHED_PRODUCT("成品件", (byte)1),
    COMPONENT("元器件", (byte)2);

    public static class Constants {
        private Constants() {
        }

        public static final String PERIPHERAL = "PERIPHERAL";
        public static final String FINISHED_PRODUCT = "FINISHED_PRODUCT";
        public static final String COMPONENT = "COMPONENT";
    }

    public final String typeDescription;
    public final Byte value;

    private InspectionType(String typeDescription, Byte value) {
        this.typeDescription = typeDescription;
        this.value = value;
    }
}

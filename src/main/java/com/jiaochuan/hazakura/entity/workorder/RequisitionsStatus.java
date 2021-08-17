package com.jiaochuan.hazakura.entity.workorder;

public enum RequisitionsStatus {
    PENDING_PURCHASE("待购买", 0),
    PURCHASED("已购买", 1),
    PENDING_ADMISSION("待入厂", 2),
    ADMISSION("已入厂", 3);

    public static class Constants {
        private Constants() {
        }

        public static final String PENDING_PURCHASE = "PENDING_PURCHASE";
        public static final String PURCHASED = "PURCHASED";
        public static final String PENDING_ADMISSION = "PENDING_ADMISSION";
        public static final String ADMISSION = "ADMISSION";
    }

    public final String statusDescription;
    public final Integer value;

    private RequisitionsStatus(String statusDescription, Integer value) {
        this.statusDescription = statusDescription;
        this.value = value;
    }
}

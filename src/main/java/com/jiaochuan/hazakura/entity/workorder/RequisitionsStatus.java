package com.jiaochuan.hazakura.entity.workorder;

public enum RequisitionsStatus {
    PENDING_APPROVAL("待审批", 0),
    PENDING_PURCHASE("待购买", 1),
    PURCHASED("已购买", 2),
    PENDING_ADMISSION("待进厂", 3),
    ADMISSION("已进厂", 4),
    REJECTED("已驳回", 5);

    public static class Constants {
        private Constants() {
        }

        public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
        public static final String PENDING_PURCHASE = "PENDING_PURCHASE";
        public static final String PURCHASED = "PURCHASED";
        public static final String PENDING_ADMISSION = "PENDING_ADMISSION";
        public static final String ADMISSION = "ADMISSION";
        public static final String REJECTED = "REJECTED";
    }

    public final String statusDescription;
    public final Integer value;

    private RequisitionsStatus(String statusDescription, Integer value) {
        this.statusDescription = statusDescription;
        this.value = value;
    }
}

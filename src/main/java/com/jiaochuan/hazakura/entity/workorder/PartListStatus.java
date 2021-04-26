package com.jiaochuan.hazakura.entity.workorder;

public enum PartListStatus {
    PENDING_FINALIZE("待确定", 0),
    PENDING_APPROVAL("待审批", 1),
    APPROVED("待出库", 2),
    READY("已出库", 3);

    public static class Constants {
        private Constants() {
        }

        public static final String PENDING_FINALIZE = "PENDING_FINALIZE";
        public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
        public static final String APPROVED = "APPROVED";
        public static final String READY = "READY";
    }

    public final String statusDescription;
    public final Integer value;

    private PartListStatus(String statusDescription, Integer value) {
        this.statusDescription = statusDescription;
        this.value = value;
    }
}

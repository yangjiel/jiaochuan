package com.jiaochuan.hazakura.entity.workorder;

public enum PartListStatus {
    PENDING_FINALIZE("待确定"),
    PENDING_APPROVAL("待审批"),
    APPROVED("待出库"),
    READY("已出库");

    public static class Constants {
        private Constants() {
        }

        public static final String PENDING_FINALIZE = "PENDING_FINALIZE";
        public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
        public static final String APPROVED = "APPROVED";
        public static final String READY = "READY";
    }

    public final String statusDescription;

    private PartListStatus(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}

package com.jiaochuan.hazakura.entity.workorder;

public enum InspectionStatus {
    PENDING_APPROVAL("待审批", 0),
    APPROVED("检验合格", 1),
    REJECTED("检验不合格", 2);

    public static class Constants {
        private Constants() {
        }

        public static final String PENDING_APPROVAL = "PENDING_APPROVAL";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
    }

    public final String statusDescription;
    public final Integer value;

    private InspectionStatus(String statusDescription, Integer value) {
        this.statusDescription = statusDescription;
        this.value = value;
    }
}

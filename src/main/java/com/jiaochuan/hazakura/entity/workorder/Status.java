package com.jiaochuan.hazakura.entity.workorder;

public enum Status {
    PENDING_FIRST_APPROVAL("待初审"),
    PENDING_FINAL_APPROVAL("待终审"),
    APPROVED("已批准"),
    PENDING_DISPATCH("待派工"),
    DISPATCHED("已派工"),
    PROCEEDING("进行中"),
    SECOND_MATERIAL("二次领料"),
    FINISHED("已完工"),
    PENDING_MATERIAL_RETURN("待返料"),
    REJECTED("已驳回"),
    COMPLETED("已截单");


    public static class Constants {
        private Constants() {}
        public static final String PENDING_FIRST_APPROVAL = "PENDING_FIRST_APPROVAL";
        public static final String PENDING_FINAL_APPROVAL = "PENDING_FINAL_APPROVAL";
        public static final String APPROVED = "APPROVED";
        public static final String PENDING_DISPATCH = "PENDING_DISPATCH";
        public static final String DISPATCHED = "DISPATCHED";
        public static final String PROCEEDING = "PROCEEDING";
        public static final String FINISHED = "FINISHED";
        public static final String PENDING_MATERIAL_RETURN = "PENDING_MATERIAL_RETURN";
        public static final String REJECTED = "REJECTED";
        public static final String COMPLETED = "COMPLETED";
    }


    public final String statusDescription;

    private Status(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}

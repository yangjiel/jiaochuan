package com.jiaochuan.hazakura.entity.workorder;

public enum ActionType {
    PRELIMINARY_MATERIAL_APPROVAL("领料初审"),
    FINAL_MATERIAL_APPROVAL("领料终审"),
    WORK_ORDER_DISPATCH("派工接单"),
    WORK_START("出勤"),
    ON_SITE_INTERRUPTION_APPROVAL("出场中断审批"),
    SECOND_MATERIAL_APPROVAL("二次领料审批"),
    COMPLETION_APPROVAL("完工审批");

    public final String typeDescription;

    private ActionType(String typeDescription) {
        this.typeDescription = typeDescription;
    }
}

package com.jiaochuan.hazakura.api.workorder;

import lombok.Data;

@Data
public class UnqualifiedDto {
    Long unqualifiedId;
    Long inspectionId;
    Long creatorId;
    Long departmentId;
    Long responsiblePersonId;
    String unqualifiedLevel;
    Integer reworkQuantity;
    String reworkDetails;
    Long operatorId;
    String reworkInspection;
    Long reworkInspectorId;
    Integer noReworkQuantity;
    Integer reworkAcceptedQuantity;
    Integer noReworkAcceptedQuantity;
    Integer rejectedQuantity;
    String influence;
    String acceptanceDetails;
    String standardOfAcceptance;
}

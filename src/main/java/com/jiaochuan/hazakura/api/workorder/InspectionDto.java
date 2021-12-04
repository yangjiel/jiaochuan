package com.jiaochuan.hazakura.api.workorder;

import com.jiaochuan.hazakura.entity.workorder.InspectionStatus;
import com.jiaochuan.hazakura.entity.workorder.InspectionType;
import lombok.Data;

@Data
public class InspectionDto {
    Long inspectionId;
    Long creatorId;
    Long departmentId;
    InspectionType type;
    String productName;
    String model;
    String serialNumber;
    Long quantity;
    String unit;
    String manufacturer;
    Byte sizeFit;
    Byte qualityCertificate;
    Byte exterior;
    Byte logo;
    Byte packaging;
    String note;
    String samplingMethod;
    Long inspectorId;
    Long requisitionsId;
    InspectionStatus status;
}

package com.jiaochuan.hazakura.api.workorder;

import com.jiaochuan.hazakura.entity.workorder.InspectionStatus;
import com.jiaochuan.hazakura.service.InspectionService;
import lombok.Data;

@Data
public class InspectionDto {
    Long inspectionId;
    Long creatorId;
    Long departmentId;
    String productName;
    String model;
    String serialNumber;
    Long quantity;
    String unit;
    String manufacturer;
    String sizeFit;
    String qualityCertificate;
    String exterior;
    String logo;
    String packaging;
    String note;
    String samplingMethod;
    Long inspectorId;
    Long requisitionsId;
    InspectionStatus status;
}

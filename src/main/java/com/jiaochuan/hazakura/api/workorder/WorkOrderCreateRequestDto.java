package com.jiaochuan.hazakura.api.workorder;

import lombok.Data;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkOrderCreateRequestDto {
    private Long customerId;
    private Long workerId;
    private LocalDate serviceDate;
    private String address;
    private List<Pair<Long, Integer>> equipments;
}

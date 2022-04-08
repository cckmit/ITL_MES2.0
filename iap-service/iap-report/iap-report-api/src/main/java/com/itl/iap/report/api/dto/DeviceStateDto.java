package com.itl.iap.report.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeviceStateDto {
    private List<String> deviceStates;

    private List<Integer> stateCounts;
}

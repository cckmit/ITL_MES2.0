package com.itl.iap.report.api.dto;

import com.itl.iap.report.api.vo.CommonVo;
import lombok.Data;

import java.util.List;

@Data
public class DeviceEfficientDto {
    List<CommonVo> commonVos;
}

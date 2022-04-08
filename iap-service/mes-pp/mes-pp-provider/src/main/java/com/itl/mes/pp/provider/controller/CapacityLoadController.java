package com.itl.mes.pp.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.CapacityLoadQueryDTO;
import com.itl.mes.pp.api.dto.CapacityLoadReportDTO;
import com.itl.mes.pp.api.service.CapacityLoadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auth liuchenghao
 * @date 2021/1/12
 */
@Api("自动排程")
@RestController
@RequestMapping("/capacityLoad")
public class CapacityLoadController {

    @Autowired
    CapacityLoadService capacityLoadService;

    @PostMapping("/list")
    @ApiOperation(value = "查询负荷报表信息", notes = "查询负荷报表信息")
    public ResponseData<CapacityLoadReportDTO> findList(@RequestBody CapacityLoadQueryDTO capacityLoadQueryDTO) {
        return ResponseData.success(capacityLoadService.findCapacityLoadList(capacityLoadQueryDTO));
    }

}

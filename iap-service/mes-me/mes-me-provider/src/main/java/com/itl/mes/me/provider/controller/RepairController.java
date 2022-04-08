package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.RepairInputDto;
import com.itl.mes.me.api.dto.ScrapOrRepairFinDto;
import com.itl.mes.me.api.entity.MeSfcRepair;
import com.itl.mes.me.api.vo.RepairLogListVo;
import com.itl.mes.me.api.vo.RepairStationVo;
import com.itl.mes.me.api.vo.SendRepairObjVo;
import com.itl.mes.me.provider.service.impl.MeSfcRepairServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
@Api(tags = "维修接口")
public class RepairController {

    @Autowired
    private MeSfcRepairServiceImpl meSfcRepairService;

    @ApiOperation(value = "维修工位-扫描SN并带出:1成品上游信息2送修明细")
    @GetMapping("/getStationInformation")
    public ResponseData<RepairStationVo> getStationInform(String param) {
        return ResponseData.success(meSfcRepairService.frontDataVo(param));
    }

    @ApiOperation(value = "维修工位-维修录入界面")
    @PostMapping("/RepairStation/inputRepairData")
    public ResponseData<SendRepairObjVo> saveInputRepair(@RequestBody List<RepairInputDto> repairInputList) {
        return ResponseData.success(meSfcRepairService.saveInputRepair(repairInputList));
    }

    @ApiOperation(value = "维修工位-报废或维修完成")
    @PostMapping("/RepairStation/scrapOrRepairFinish")
    public ResponseData<Integer> scrapOrRepairFinish(@RequestBody ScrapOrRepairFinDto scrapOrRepairFinDto) {
        return ResponseData.success(meSfcRepairService.scrapOrRepairFinish(scrapOrRepairFinDto));
    }
}

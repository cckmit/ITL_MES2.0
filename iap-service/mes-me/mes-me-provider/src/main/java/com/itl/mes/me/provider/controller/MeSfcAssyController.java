package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.BomComponnetVo;
import com.itl.mes.me.api.dto.ItemWithTemplateDto;
import com.itl.mes.me.api.dto.MeSfcAssyAssyDto;
import com.itl.mes.me.api.service.MeSfcAssyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/1/26
 * @since JDK1.8
 */
@RestController
@Api(tags = "装配")
@RequestMapping("/assy")
public class MeSfcAssyController {
    @Autowired
    private MeSfcAssyService meSfcAssyService;

    @PostMapping("/getAssyList")
    @ApiOperation("扫描成品SN(根据成品sn和成品物料编号等获取装配清单)")
    public ResponseData<List<BomComponnetVo>> getAssyList(@RequestBody MeSfcAssyAssyDto assyDto) throws CommonException {
        return ResponseData.success(meSfcAssyService.getAssyList(assyDto));
    }

    @GetMapping("/assyComponentBySn")
    @ApiOperation("扫描物料SN同时装配(根据成品sn和组件sn获取组件物料编码)")
    public ResponseData<String> assyComponent(
            @RequestParam("sn") String sn,
            @RequestParam("componentSn") String componentSn) throws CommonException {
        return ResponseData.success(meSfcAssyService.assyComponentBySn(sn, componentSn));
    }

    @GetMapping("/sop")
    @ApiOperation("SOP")
    public ResponseData<List<ItemWithTemplateDto>> getTemplates(@RequestParam("station") String station) {
        return ResponseData.success(meSfcAssyService.getTemplates(station));
    }
}

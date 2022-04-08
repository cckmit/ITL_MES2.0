package com.itl.iap.report.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.dto.ShopOrderTrackDto;
import com.itl.iap.report.api.entity.ShopOrderTrack;
import com.itl.iap.report.api.service.ShopOrderTrackService;
import com.itl.iap.report.api.vo.ShopOrderTrackVo;
import com.itl.iap.report.provider.service.impl.ShopOrderTrackServiceImpl;
import com.itl.iap.report.provider.util.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/shopOrderTrack")
@Api(tags = "工单跟踪报表" )
public class ShopOrderTrackController {

    @Autowired
    private ShopOrderTrackService shopOrderTrackService;

    @Autowired
    private ShopOrderTrackServiceImpl shopOrderTrackServiceImpl;

    @PostMapping("/query")
    @ApiOperation("分页查询工单跟踪报表信息")
    public ResponseData<ShopOrderTrackVo> query(@RequestBody ShopOrderTrackDto shopOrderTrackDto){
       // return ResponseData.success(shopOrderTrackService.selectByCondition(shopOrderTrackDto));
        return  ResponseData.success(shopOrderTrackServiceImpl.selectInfoByCondition(shopOrderTrackDto));
    }

    @PostMapping ("/export")
    @ApiOperation(value = "导出文件")
    public void export(@RequestBody ShopOrderTrackDto shopOrderTrackDto,HttpServletResponse response) throws CommonException {
        //shopOrderTrackService.export(shopOrderTrackDto,response);
        shopOrderTrackServiceImpl.exportInfo(shopOrderTrackDto,response);
    }
}

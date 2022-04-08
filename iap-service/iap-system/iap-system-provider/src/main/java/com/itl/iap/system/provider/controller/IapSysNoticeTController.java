package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysNoticeTDto;
import com.itl.iap.system.api.entity.IapSysNoticeT;
import com.itl.iap.system.api.service.IapSysNoticeTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告Controller
 *
 * @author 李骐光
 * @date 2020-06-22
 * @since jdk1.8
 */
@Api("System-公告管理控制层")
@RestController
@RequestMapping("/notice")
public class IapSysNoticeTController {
    @Autowired
    private IapSysNoticeTService noticeService;

    @PostMapping("/query")
    @ApiOperation(value = "查询公告", notes = "查询公告")
    public ResponseData<IPage<IapSysNoticeTDto>> query(@RequestBody IapSysNoticeTDto noticeDto) {
        return ResponseData.success(noticeService.query(noticeDto));
    }

    @PostMapping("/getPlatform")
    @ApiOperation(value = "查询平台公告", notes = "查询平台公告")
    public ResponseData<IPage<IapSysNoticeTDto>> getPlatform(@RequestBody IapSysNoticeTDto noticeDto) {
        return ResponseData.success(noticeService.getPlatform(noticeDto));
    }

    @PostMapping("/getEnterprise")
    @ApiOperation(value = "查询企业公告", notes = "查询企业公告")
    public ResponseData<IPage<IapSysNoticeTDto>> getEnterprise(@RequestBody IapSysNoticeTDto noticeDto) {
        return ResponseData.success(noticeService.getEnterprise(noticeDto));
    }

    @PostMapping("/getNoticeById")
    @ApiOperation(value = "查询公告详情", notes = "查询公告详情")
    public ResponseData<IapSysNoticeT> getNoticeById(@RequestBody IapSysNoticeTDto noticeDto) {
        return ResponseData.success(noticeService.getNoticeById(noticeDto));
    }

    @PostMapping("/addNotice")
    @ApiOperation(value = "新建公告", notes = "新建公告")
    public ResponseData<String> addNotice(@RequestBody IapSysNoticeTDto noticeDto) {
        return ResponseData.success(noticeService.addNotice(noticeDto));
    }

    @PostMapping("/updateNoticeDto")
    @ApiOperation(value = "修改公告", notes = "修改公告")
    public ResponseData<String> updateNoticeDto(@RequestBody IapSysNoticeTDto noticeDto) {
        return ResponseData.success(noticeService.updateNoticeDto(noticeDto));
    }

    @PostMapping("/deleteNoticeById")
    @ApiOperation(value = "批量删除公告", notes = "批量删除公告")
    public ResponseData<Boolean> deleteNoticeById(@RequestBody List<IapSysNoticeTDto> sysNoticeDtoList) {
        return ResponseData.success(noticeService.deleteNoticeById(sysNoticeDtoList));
    }

    @PostMapping("/publishById/{id}")
    @ApiOperation(value = "发布", notes = "发布")
    public ResponseData<String> publishById(@PathVariable("id") String id) {
        return ResponseData.success(noticeService.publishById(id));
    }

}

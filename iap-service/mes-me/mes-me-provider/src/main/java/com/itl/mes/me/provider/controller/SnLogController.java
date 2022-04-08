package com.itl.mes.me.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.SnLogDto;
import com.itl.mes.me.api.entity.SnLog;
import com.itl.mes.me.api.service.SnLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * SN日志表
 *
 * @author cuichonghe
 * @date 2020-12-25 12:36:32
 */
@RestController
@RequestMapping("me/snLog")
@Api(tags = "SN日志表")
public class SnLogController {
    @Autowired
    private SnLogService snLogService;


    /**
     * 分页查询信息
     *
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询SN日志信息")
    public ResponseData<IPage<SnLog>> page(@RequestBody SnLogDto snLogDto) {
        return ResponseData.success(snLogService.getAll(snLogDto));
    }

    /**
     * 信息
     */
    @GetMapping("/getOne/{bo}")
    public ResponseData info(@PathVariable("bo") String bo){
		SnLog snLog = snLogService.getById(bo);
        return  ResponseData.success(snLog);
    }

    /**
     * 保存/修改
     */
    @PostMapping("/save")
    public ResponseData save(@RequestBody SnLog snLog){
		snLogService.saveSnLogs(snLog);

        return ResponseData.success();
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody String[] bos){
		snLogService.removeByIds(Arrays.asList(bos));

        return ResponseData.success();
    }


    /**
     * excel导出
     *
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportItem(HttpServletResponse response) {
        snLogService.exportOperaton(response);
    }


}

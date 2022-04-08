package com.itl.iap.workflow.workflow.controller;
import com.itl.iap.common.base.controller.BaseController;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.workflow.api.dto.ActHiNotifyDto;
import com.itl.iap.workflow.api.entity.ActHiNotify;
import com.itl.iap.workflow.workflow.service.IActHiNotifyService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 抄送通知(ActHiNotify)表控制层
 *
 * @author 罗霄
 * @date 2020-09-16
 * @since jdk1.8
 */
@RestController
@RequestMapping("/actHiNotify")
public class ActHiNotifyController extends BaseController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private IActHiNotifyService actHiNotifyService;
    
    @PostMapping("/add")
    @ApiOperation(value = "新增记录", notes = "新增记录")
    public ResponseData add(@RequestBody ActHiNotify actHiNotify) {
        logger.info("ActHiNotifyDto add Record...");
        return ResponseData.success(actHiNotifyService.saveOrUpdate(actHiNotify));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据ID删除记录", notes = "根据ID删除记录")
    public ResponseData delete(@RequestBody ActHiNotify actHiNotify) {
        logger.info("ActHiNotifyDto delete Record...");
        return ResponseData.success(actHiNotifyService.removeById(actHiNotify.getId()));
    }
    
    @PostMapping("/update")
    @ApiOperation(value = "根据ID修改记录", notes = "根据ID修改记录")
    public ResponseData update(@RequestBody ActHiNotify actHiNotify) {
        logger.info("ActHiNotifyDto updateRecord...");
        return ResponseData.success(actHiNotifyService.updateById(actHiNotify));
    }
    
    @PostMapping("/query")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ResponseData queryRecord(@RequestBody ActHiNotifyDto actHiNotifyDto) {
        logger.info("ActHiNotifyDto queryRecord...");
        return ResponseData.success(actHiNotifyService.pageQuery(actHiNotifyDto));
    }
}
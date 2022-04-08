package com.itl.iap.notice.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.notice.api.dto.MsgMailConfigurationDto;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;
import com.itl.iap.notice.api.pojo.ResponseCode;
import com.itl.iap.notice.api.service.MsgMailConfigurationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发送规则Controller
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@RestController
@RequestMapping("/msgMailConfiguration")
public class MsgMailConfigurationController {

    @Autowired
    private MsgMailConfigurationService msgMailConfigurationService;


    @PostMapping("/addOrUpdate")
    @ApiOperation(value = "新增邮件的基础配置", notes = "新增邮件的基础配置")
    public ResponseData<MsgMailConfiguration> addOrUpdate(@RequestBody MsgMailConfiguration msgMailConfiguration) {
//        MsgMailConfigurationService bean = SpringUtil.getBean(MsgMailConfigurationService.class);
//        bean.addOrUpdate(msgMailConfiguration);
        msgMailConfigurationService.addOrUpdate(msgMailConfiguration);
//        msgMailConfigurationService.addOrUpdate(msgMailConfiguration);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据Id查询", notes = "根据Id查询")
    public ResponseData<MsgMailConfiguration> add(@PathVariable("id") String id) {
        return new ResponseData<MsgMailConfiguration>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), msgMailConfigurationService.findById(id));
    }

    @PostMapping("/query")
    @ApiOperation(value = "根据属性查询", notes = "根据属性查询")
    public ResponseData<IPage<MsgMailConfigurationDto>> query(@RequestBody MsgMailConfiguration msgMailConfiguration, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return new ResponseData<IPage<MsgMailConfigurationDto>>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), msgMailConfigurationService.queryPage(msgMailConfiguration, page, pageSize));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    public ResponseData<MsgMailConfiguration> delete(@PathVariable("id") String id) {
        msgMailConfigurationService.delete(id);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @GetMapping("/startUsing/{id}")
    @ApiOperation(value = "根据id启用邮箱或短信", notes = "根据id启用邮箱或短信")
    public ResponseData<MsgMailConfiguration> startUsing(@PathVariable("id") String id, @RequestParam("enable") Integer enable) {
        msgMailConfigurationService.startUsing(id, enable);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @DeleteMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public ResponseData<MsgMailConfiguration> deleteBatch(@RequestBody List<MsgMailConfiguration> msgMailConfigurationList) {
        msgMailConfigurationService.deleteBatch(msgMailConfigurationList);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

}

package com.itl.mes.me.provider.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.entity.InstructorItemTemplate;
import com.itl.mes.me.api.service.InstructorItemTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@RestController
@RequestMapping("/instructorItemTemplate")
@Api(tags = "作业指导书内容模板")
public class InstructorItemTemplateController {
    @Autowired
    private InstructorItemTemplateService templateService;

    @PostMapping("/get/{itemBo}")
    @ApiOperation(value = "查看模板")
    public ResponseData<InstructorItemTemplate> getByItemBo(@PathVariable("itemBo") String itemBo) {
        InstructorItemTemplate template = templateService.getById(itemBo);
        return ResponseData.success(template);
    }

    @PostMapping("/save")
    @ApiOperation(value = "编辑模板")
    public ResponseData<String> save(@RequestBody InstructorItemTemplate template) throws CommonException {
        if (template == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        templateService.saveTo(template);
        return ResponseData.success("success");
    }
}

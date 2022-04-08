package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.InstructorVarDto;
import com.itl.mes.me.api.entity.InstructorVar;
import com.itl.mes.me.api.service.InstructorVarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@RestController
@RequestMapping("/instructorVar")
@Api(tags = "作业指导书变量")
public class InstructorVarController {
    @Autowired
    private InstructorVarService instructorVarService;

    @PostMapping("/list/{instructorBo}")
    @ApiOperation(value = "查询列表")
    public ResponseData<List<InstructorVar>> list(@PathVariable("instructorBo") String instructorBo) throws CommonException {
        return ResponseData.success(instructorVarService.listAll(instructorBo));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResponseData<String> save(@RequestBody InstructorVarDto instructorVarDto) throws CommonException {
        if (instructorVarDto == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        instructorVarService.saveAndUpdate(instructorVarDto);
        return ResponseData.success("success");
    }

    @DeleteMapping("/delete/{varBo}")
    @ApiOperation(value = "删除,path传参")
    public ResponseData<String> delete(@PathVariable("varBo") String varBo) throws CommonException {
        boolean b = instructorVarService.removeById(varBo);
        if (b) {
            return ResponseData.success("success");
        } else {
            throw new CommonException("删除失败", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }
}

package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.me.api.dto.InstructorOperationDto;
import com.itl.mes.me.api.service.InstructorOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@RestController
@RequestMapping("/instructorOperation")
@Api(tags = "作业指导书分配工序")
public class InstructorOperationController {
    @Autowired
    private InstructorOperationService instructorOperationService;

    @PostMapping("/getAssigned/{instructorBo}")
    @ApiOperation(value = "获取已分配和未分配的工序")
    public ResponseData<Map<String, List<Operation>>> getAssigned(@PathVariable("instructorBo") String instructorBo) {
        Map<String, List<Operation>> operations = instructorOperationService.getOperations(instructorBo);
        return ResponseData.success(operations);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存工序")
    public ResponseData<String> save(@RequestBody InstructorOperationDto instructorOperationDto) throws CommonException {
        try {
            instructorOperationService.saveOperations(instructorOperationDto);
        } catch (Exception e) {
            throw new CommonException("保存失败", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ResponseData.success("success");
    }
}

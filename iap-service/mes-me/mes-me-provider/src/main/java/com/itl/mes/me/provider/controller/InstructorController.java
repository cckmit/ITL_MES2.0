package com.itl.mes.me.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.me.api.bo.InstructorHandleBo;
import com.itl.mes.me.api.dto.InstructorQueryDto;
import com.itl.mes.me.api.dto.InstructorSaveDto;
import com.itl.mes.me.api.entity.Instructor;
import com.itl.mes.me.api.service.InstructorService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
@RestController
@RequestMapping("/instructor")
@Api(tags = "作业指导书")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询")
    public ResponseData<IPage<Instructor>> page(@RequestBody InstructorQueryDto queryDto) throws CommonException {
        return ResponseData.success(instructorService.queryPage(queryDto));
    }

    @PostMapping("/pageByState")
    @ApiOperation(value = "分页查询ByState")
    public ResponseData<IPage<Instructor>> pageByState(@RequestBody InstructorQueryDto queryDto) throws CommonException {
        return ResponseData.success(instructorService.queryPageByState(queryDto));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResponseData<String> save(@RequestBody InstructorSaveDto instructorSaveDto) throws CommonException {
        if (instructorSaveDto == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        instructorService.saveAndUpdate(instructorSaveDto);
        return ResponseData.success("success");
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public ResponseData<String> delete(@RequestBody String[] bos) throws CommonException {
        instructorService.delete(bos);
        return ResponseData.success("success");
    }

    @GetMapping("/getInstructor/{instructor}/{version}")
    @ApiOperation(value = "根据编号和版本查询指导书")
    public ResponseData<Instructor> getInstructor(@PathVariable("instructor") String instructor, @PathVariable("version") String version) {
        String bo = new InstructorHandleBo(UserUtils.getSite(), instructor, version).getBo();
        return ResponseData.success(instructorService.getById(bo));
    }
}

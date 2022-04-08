package com.itl.iap.system.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import com.itl.iap.system.api.dto.IapEmployeeTDto;
import com.itl.iap.system.api.service.IapEmployeeTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 员工管理Controller
 *
 * @author 马家伦
 * @date 2020-06-17
 * @since jdk1.8
 */
@Api("System-员工管理控制层")
@RestController
@RequestMapping("/iapEmployeeT")
public class IapSysEmployeeTController {

    @Resource
    IapEmployeeTService iapEmployeeService;

    @PostMapping("/query")
    @ApiOperation(value = "通过条件查询员工列表", notes = "通过条件查询员工列表")
    public ResponseData<IPage<IapEmployeeTDto>> query(@RequestBody IapEmployeeTDto iapEmployeeDto) {
        return ResponseData.success(iapEmployeeService.query(iapEmployeeDto));
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加新员工", notes = "添加新员工")
    public ResponseData<String> add(@RequestBody @Validated({ValidationGroupAdd.class}) IapEmployeeTDto iapEmployeeDto) {
        return ResponseData.success(iapEmployeeService.add(iapEmployeeDto));
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新员工信息", notes = "更新员工信息")
    public ResponseData<String> update(@RequestBody @Validated({ValidationGroupUpdate.class}) IapEmployeeTDto iapEmployeeDto) {
        return ResponseData.success(iapEmployeeService.update(iapEmployeeDto));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除员工", notes = "删除员工")
    public ResponseData<Boolean> delete(@RequestBody IapEmployeeTDto iapEmployeeDto) {
        return ResponseData.success(iapEmployeeService.deleteById(iapEmployeeDto));
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "批量删除员工", notes = "批量删除员工")
    public ResponseData<Boolean> deleteByIds(@RequestBody List<IapEmployeeTDto> iapEmployeeDtoList) {
        return ResponseData.success(iapEmployeeService.deleteByIapEmployeeDtoList(iapEmployeeDtoList));
    }
    @PostMapping("/updateUserId")
    @ApiOperation(value = "删除员工关联账号", notes = "删除员工关联账号")
    public ResponseData<Boolean> updateUserId(@RequestBody IapEmployeeTDto iapEmployeeDtoList) {
        return ResponseData.success(iapEmployeeService.updateUserId(iapEmployeeDtoList));
    }

}
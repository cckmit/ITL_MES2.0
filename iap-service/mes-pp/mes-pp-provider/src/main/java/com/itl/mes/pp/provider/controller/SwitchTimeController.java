package com.itl.mes.pp.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.mes.core.api.vo.StationVo;
import com.itl.mes.pp.api.dto.SwitchTimeSaveDto;
import com.itl.mes.pp.api.entity.SwitchTimeEntity;
import com.itl.mes.pp.api.service.SwitchTimeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/17
 * @since JDK1.8
 */
@Api(tags = "生产切换时间控制层")
@RestController
@RequestMapping("/p/switchTime")
public class SwitchTimeController {

    @Autowired
    SwitchTimeService switchTimeService;

    @PostMapping("/save")
    @ApiOperation(value = "保存生产切换时间数据")
    public ResponseData<String> save(@RequestBody SwitchTimeSaveDto switchTimeSaveDto) throws CommonException {
        if (switchTimeSaveDto == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        switchTimeService.saveAndUpdate(switchTimeSaveDto);
        return ResponseData.success("success");
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询生产切换时间")
    @ApiOperationSupport(params = @DynamicParameters(name = "SwitchTimeRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面,默认1"),
            @DynamicParameter(name = "limit", value = "分页大小,默认100,可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性,可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式,true/false,可不填"),
            @DynamicParameter(name = "processCharacteristics", value = "工艺特性"),
            @DynamicParameter(name = "targetProcessCharacteristics", value = "目标工艺特性"),
            @DynamicParameter(name = "switchType", value = "切换类型")
    }))
    public ResponseData<IPage<SwitchTimeEntity>> page(@RequestBody Map<String, Object> params) throws CommonException {
        return ResponseData.success(switchTimeService.queryPage(params));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除生产切换时间数据")
    public ResponseData<String> delete(@RequestBody String[] bos) throws CommonException {
        switchTimeService.delete(bos);
        return ResponseData.success("success");
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportItem(HttpServletResponse response) {
        switchTimeService.exportOperation(response);
    }

    @PostMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        List<SwitchTimeEntity> list = ExcelUtils.importExcel(file, 0, 1, SwitchTimeEntity.class);
        try {
            switchTimeService.saveBatch(list);
        } catch (Exception e) {
            throw new CommonException("导入失败", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ResponseData.success("success");
    }
}

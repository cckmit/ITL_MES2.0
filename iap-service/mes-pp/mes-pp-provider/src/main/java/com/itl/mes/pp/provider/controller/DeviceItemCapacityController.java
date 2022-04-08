package com.itl.mes.pp.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.client.service.WorkShopService;
import com.itl.mes.pp.api.dto.MachineProductCapacityDto;
import com.itl.mes.pp.api.entity.DeviceItemCapacityEntity;
import com.itl.mes.pp.api.service.DeviceItemCapacityService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 机台产品产能表控制层
 *
 * @author cuichonghe
 * @date 2020-12-17 11:28:39
 */
@RestController
@Api(tags = "机台产品产能表控制层")
@RequestMapping("deviceItemCapacity")
public class DeviceItemCapacityController {
    @Autowired
    private DeviceItemCapacityService deviceItemCapacityService;
    @Autowired
    private WorkShopService workShopService;

    /**
     * 查询全部
     */
    @ApiOperation(value = "查询全部")
    @PostMapping("/getAll")
    public ResponseData<IPage<DeviceItemCapacityEntity>> getAll(@RequestBody MachineProductCapacityDto machineProductCapacityDto) {
        return ResponseData.success(deviceItemCapacityService.getPage(machineProductCapacityDto));
    }

    /**
     * 查询工艺特性
     */
    @ApiOperation(value = "查询工艺特性")
    @PostMapping("/getProcessCharacteristics")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "PushRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填")
                    }))
    public ResponseData<IPage<Map<String,Object>>> get(@RequestBody Map<String, Object> params) {
        IPage<Map<String, Object>> ret = deviceItemCapacityService.pageMaps(new QueryPage<>(params)
                , new QueryWrapper<DeviceItemCapacityEntity>().select("PROCESS_CHARACTERISTICS").eq("SITE", UserUtils.getSite()).groupBy("PROCESS_CHARACTERISTICS"));

        return ResponseData.success(ret);
    }

    /**
     * 通过工厂查询数据
     *
     * @return
     */
    @ApiOperation(value = "通过工厂查询数据")
    @GetMapping("/getWorkShopBySite")
    public ResponseData getWorkShopBySite() {
        return ResponseData.success(workShopService.getWorkShopBySite());
    }

    /**
     * 查询单条数据
     */
    @GetMapping("/get/{bo}")
    @ApiOperation(value = "查询单条数据")
    public ResponseData get(@PathVariable("bo") String bo) {
        DeviceItemCapacityEntity MachineProductCapacity = deviceItemCapacityService.getOneById(bo);
       return ResponseData.success(MachineProductCapacity);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public ResponseData save(@RequestBody DeviceItemCapacityEntity pMachineProductCapacity) {
        deviceItemCapacityService.saveMachineProductCapacityEntity(pMachineProductCapacity);
        return ResponseData.success();
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody Integer[] bos) {
        deviceItemCapacityService.removeByIds(Arrays.asList(bos));
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
        deviceItemCapacityService.exportOperaton(response);
    }


    @PostMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        List<DeviceItemCapacityEntity> machineProductCapacityEntities = ExcelUtils.importExcel(file, 1, 1, DeviceItemCapacityEntity.class);
        machineProductCapacityEntities.forEach(x -> deviceItemCapacityService.saveMachineProductCapacityEntity(x));
        return ResponseData.success("success");
    }
}

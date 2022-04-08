package com.itl.mes.pp.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.ProductLineDto;
import com.itl.mes.pp.api.entity.DeviceCapacityEntity;
import com.itl.mes.pp.api.service.DeviceCapacityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


/**
 * 产线表
 *
 * @author cuichonghe
 * @date 2020-12-18 14:05:47
 */
@Api(tags = "机台产能控制层")
@RestController
@RequestMapping("p/deviceCapacity")
public class DeviceCapacityController {
    @Autowired
    private DeviceCapacityService deviceCapacityService;

    /**
     * 查询全部
     */
    @PostMapping("/getAll")
    @ApiOperation(value = "查询全部")
    public ResponseData<IPage<DeviceCapacityEntity>> list(@RequestBody ProductLineDto productLineDto) {
        return ResponseData.success(deviceCapacityService.getAll(productLineDto));
    }


    /**
     * 保存
     */
    @ApiOperation(value = "保存或修改")
    @PostMapping("/save")
    public ResponseData save(@RequestBody DeviceCapacityEntity productLineEntities) throws CommonException {
        deviceCapacityService.saveProductLine(productLineEntities);

        return ResponseData.success();
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody String[] bos) {
        deviceCapacityService.removeByIds(Arrays.asList(bos));

        return ResponseData.success();
    }

}

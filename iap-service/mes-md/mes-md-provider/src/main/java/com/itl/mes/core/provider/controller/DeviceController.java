package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.api.vo.DeviceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-06-17
 */
@RestController
@RequestMapping("/devices")
@Api(tags = " 设备表" )
public class DeviceController {
    private final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public DeviceService deviceService;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Device> getDeviceById(@PathVariable String id) {
        return ResponseData.success(deviceService.getDeviceById(id));
    }

    @PostMapping("/selectDeviceWorkshop")
    @ApiOperation(value = "分页查询机台车间信息", notes = "分页查询机台车间信息")
    public ResponseData<IPage<Device>> findList(@RequestBody DeviceDto deviceDto) {
        return ResponseData.success(deviceService.selectDeviceWorkshop(deviceDto));
    }
    /**
     * 保存设备数据
     *
     * @param deviceVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value="保存设备数据")
    public ResponseData<DeviceVo> saveDevice(@RequestBody DeviceVo deviceVo ) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(deviceVo);
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        deviceService.saveDevice(deviceVo);
        deviceVo = deviceService.getDeviceVoByDevice(deviceVo.getDevice());
        return ResponseData.success( deviceVo );
    }


    /**
     * 根据device查询
     *
     * @param device 设备编号
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value="通过设备编号查询数据")
    @ApiImplicitParam(name="device",value="设备编号",dataType="string", paramType = "query")
    public ResponseData<DeviceVo> getDeviceVoByDevice(String device ) throws CommonException {
        DeviceVo result = null;
        if (StrUtil.isBlank(device)) {
            throw new CommonException("设备编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        result = deviceService.getDeviceVoByDevice(device);
        if( result==null ){
            throw new CommonException( "设备编号"+device+"数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        return ResponseData.success(result);
    }

    /**
     * 删除设备数据
     *
     * @param device
     * @return
     */
    @DeleteMapping( "/delete" )
    @ApiOperation( value="删除设备数据" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="device",value="设备编号",dataType="string", paramType = "query"),
            // @ApiImplicitParam(name="modifyDate",value="修改时间",dataType="string", paramType = "query")
    })
    public ResponseData<String> deleteDevice(String device ) throws CommonException {
        if( StrUtil.isBlank( device ) ){
            throw new CommonException( "设备参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        /*if (StrUtil.isBlank(modifyDate)){
            throw new CommonException( "修改日期不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }*/
        deviceService.deleteDevice( device.trim() );
        return ResponseData.success( "success" );
    }

    @GetMapping("/getDeviceTypeVoList")
    @ApiOperation(value="查询前500条设备类型数据页面初始化用")
    public ResponseData<List<DeviceTypeVo>> getDeviceTypeVoList() {
        List<DeviceTypeVo> deviceTypeVoList = deviceService.getDeviceTypeVoList();
        return ResponseData.success(deviceTypeVoList);
    }



    @PostMapping("/getScrewByLine")
    @ApiOperation(value="查询前500条设备类型数据页面初始化用")
    public ResponseData getScrewByLine(@RequestBody Map<String, Object> params) {
        return ResponseData.success(deviceService.getScrewByLine(params));
    }

    /**
     * 分页查询设备信息
     * @param deviceDto
     * @return
     */
    @PostMapping("/queryList")
    @ApiOperation(value="分页查询设备信息")
    public ResponseData<IPage<Device>> queryList(@RequestBody DeviceDto deviceDto){
        return ResponseData.success(deviceService.query(deviceDto));
    }

    /**
     * 根据device查询已分配设备类型和未分配设备类型
     *
     * @param device 设备编号
     * @return
     */
    @GetMapping("/queryType")
    @ApiOperation(value="通过设备编号查询设备类型")
    @ApiImplicitParam(name="device",value="设备编号",dataType="string", paramType = "query")
    public ResponseData<DeviceVo> getDeviceVoTypeByDevice(String device ) throws CommonException {
        DeviceVo result = null;
        if (StrUtil.isBlank(device)) {
            throw new CommonException("设备编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        result = deviceService.getDeviceVoTypeByDevice(device);
        /*if( result==null ){
            throw new CommonException( "设备编号"+device+"数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }*/
        return ResponseData.success(result);
    }

    @GetMapping("/queryDeviceByType")
    @ApiOperation(value="通过设备类型查询所有的设备")
    public ResponseData<List<Device>> queryDeviceByType(String type ){
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_type",type);
        return ResponseData.success(deviceService.list(queryWrapper));
    }
}
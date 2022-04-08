package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.service.DeviceTypeService;
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

/**
 *
 * @author space
 * @since 2019-06-17
 */
@RestController
@RequestMapping("/deviceTypes")
@Api(tags = " 设备类型表" )
public class DeviceTypeController {
    private final Logger logger = LoggerFactory.getLogger(DeviceTypeController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public DeviceTypeService deviceTypeService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<DeviceType> getDeviceTypeById(@PathVariable String id) {
        return ResponseData.success(deviceTypeService.getById(id));
    }

    /**
     * 保存设备类型数据
     *
     * @param deviceTypeVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value="保存设备类型数据")
    public ResponseData<DeviceTypeVo> saveDeviceType(@RequestBody DeviceTypeVo deviceTypeVo ) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(deviceTypeVo);
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        deviceTypeService.saveDeviceType(deviceTypeVo);
        deviceTypeVo = deviceTypeService.getDeviceTypeVoByDeviceType(deviceTypeVo.getDeviceType());
        return ResponseData.success( deviceTypeVo );
    }

    /**
     * 根据device查询
     *
     * @param deviceType 设备类型编号
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value="通过设备类型编号查询数据")
    @ApiImplicitParam(name="deviceType",value="设备类型编号",dataType="string", paramType = "query")
    public ResponseData<DeviceTypeVo> getDeviceTypeVoByDeviceType(String deviceType ) throws CommonException {
        DeviceTypeVo result = null;
        if (StrUtil.isBlank(deviceType)) {
            throw new CommonException("设备类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        result = deviceTypeService.getDeviceTypeVoByDeviceType(deviceType);
        if( result==null ){
            throw new CommonException( "设备类型"+deviceType+"数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        return ResponseData.success(result);
    }

    /**
     * 删除设备类型数据
     *
     * @param deviceType
     *
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation( value="删除设备类型数据" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="deviceType",value="设备类型",dataType="string", paramType = "query"),
            //@ApiImplicitParam(name="modifyDate",value="修改时间",dataType="string", paramType = "query")
    })
    public ResponseData<String> deleteDeviceType(String deviceType) throws CommonException {
        if( StrUtil.isBlank( deviceType ) ){
            throw new CommonException( "设备类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        /*if( StrUtil.isBlank( modifyDate ) ){
            throw new CommonException( "修改时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }*/
        deviceTypeService.deleteDeviceType( deviceType.trim() );
        return ResponseData.success( "success" );
    }

    @GetMapping("/getDeviceVoList")
    @ApiOperation(value="查询前500条设备数据页面初始化用")
    public ResponseData<List<DeviceVo>> getDeviceVoList() {
        List<DeviceVo> deviceVoList = deviceTypeService.getDeviceVoList();
        return ResponseData.success(deviceVoList);
    }

}
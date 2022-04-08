package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.service.CarrierTypeService;
import com.itl.mes.core.api.vo.CarrierTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author space
 * @since 2019-07-22
 */
@RestController
@RequestMapping("/carrierTypes")
@Api(tags = " 载具类型表" )
public class CarrierTypeController {
    private final Logger logger = LoggerFactory.getLogger(CarrierTypeController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public CarrierTypeService carrierTypeService;



    @PostMapping("/save")
    @ApiOperation(value = "保存载具类型")
    public ResponseData<CarrierTypeVo> save(@RequestBody CarrierTypeVo carrierTypeVo) throws CommonException {
        carrierTypeService.save(carrierTypeVo);
        carrierTypeVo = carrierTypeService.getCarrierTypeVoByCarrierType(carrierTypeVo.getCarrierType());
        return ResponseData.success(carrierTypeVo);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询载具类型信息")
    public ResponseData<CarrierTypeVo> getCarrierTypeVo(String carrierType) throws CommonException {
        CarrierTypeVo carrierTypeVo = carrierTypeService.getCarrierTypeVoByCarrierType(carrierType);
        return ResponseData.success(carrierTypeVo);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除载具维护类型信息")
    public ResponseData<String> delete(String carrierType,String modifyDate) throws CommonException {
        carrierTypeService.delete(carrierType, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }


}
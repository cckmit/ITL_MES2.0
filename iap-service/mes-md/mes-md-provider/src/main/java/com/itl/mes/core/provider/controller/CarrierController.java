package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.service.CarrierService;
import com.itl.mes.core.api.vo.CarrierVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author space
 * @since 2019-07-23
 */
@RestController
@RequestMapping("/carriers")
@Api(tags = " 载具表" )
public class CarrierController {
    private final Logger logger = LoggerFactory.getLogger(CarrierController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public CarrierService carrierService;


    @PostMapping("/save")
    @ApiOperation(value = "保存载具信息")
    public ResponseData<CarrierVo> save(@RequestBody CarrierVo carrierVo) throws CommonException {
        carrierService.save(carrierVo);
        carrierVo= carrierService.getCarrierVoByCarrier(carrierVo.getCarrier());
        return ResponseData.success(carrierVo);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询载具信息")
    public  ResponseData<CarrierVo> selectCarrierVo(String carrier) throws CommonException {
        CarrierVo carrierVo = carrierService.getCarrierVoByCarrier(carrier);
        return ResponseData.success(carrierVo);
    }


    @GetMapping("/delete")
    @ApiOperation(value = "删除载具信息")
    public ResponseData<String> delete(String carrier,String modifyDate) throws CommonException {
        carrierService.delete(carrier, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }


}
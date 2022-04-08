package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.Shift;
import com.itl.mes.core.api.service.ShiftService;
import com.itl.mes.core.api.vo.ShiftVo;
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
 * @since 2019-06-19
 */
@RestController
@RequestMapping("/shifts")
@Api(tags = " 班次信息主表" )
public class ShiftController {
    private final Logger logger = LoggerFactory.getLogger(ShiftController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ShiftService shiftService;

    /**
    * 根据id查询
    *
    * @param id 主键
    * @return
    */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<Shift> getShiftById(@PathVariable String id) {
        return ResponseData.success(shiftService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存班次信息")
    public ResponseData<ShiftVo> save(@RequestBody ShiftVo shiftVo) throws CommonException {
        shiftService.save(shiftVo);
        shiftVo = shiftService.getShiftVoByShift(shiftVo.getShift());
        return ResponseData.success(shiftVo);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询班次信息")
    public ResponseData<ShiftVo>  getShiftVoByShift(String shift) throws CommonException {
        ShiftVo shiftVo = shiftService.getShiftVoByShift(shift);
        return ResponseData.success(shiftVo);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除班次信息")
    public ResponseData<String> delete(String shift,String modifyDate) throws CommonException {
        shiftService.delete(shift, DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }


}
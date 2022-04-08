package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.service.PackingService;
import com.itl.mes.core.api.vo.PackingVo;
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
 * @since 2019-07-12
 */
@RestController
@RequestMapping("/packings")
@Api(tags = " 包装定义表" )
public class PackingController {
    private final Logger logger = LoggerFactory.getLogger(PackingController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public PackingService packingService;


    @PostMapping("/save")
    @ApiOperation(value="保存包装信息")
    public ResponseData<PackingVo> save(@RequestBody PackingVo packingVo) throws CommonException {
        packingService.save(packingVo);
        packingVo = packingService.getPackingVoByPackName(packingVo.getPackName());
        return ResponseData.success(packingVo);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询包装信息")
    public ResponseData<PackingVo> getPackingVo(String packName) throws CommonException {
        PackingVo packingVo= packingService.getPackingVoByPackName(packName);
        return ResponseData.success(packingVo);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除包装信息")
    public ResponseData<String> delete(String packName,String modifyDate) throws CommonException {
        packingService.delete(packName, DateUtil.parse(modifyDate));
        return ResponseData.success("success");
    }

}
package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.MeSfcHandleBO;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.service.MeSfcNcLogService;
import com.itl.mes.me.api.service.MeSfcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


/**
 * Sfc不合格记录表
 *
 * @author 崔翀赫
 * @date 2021/1/26$
 * @since JDK1.8
 */
@RestController
@Api(tags = "检查工位")
@RequestMapping("meSfcNcLog")
public class MeSfcNcLogController {
    @Autowired
    private MeSfcNcLogService meSfcNcLogService;
    @Autowired
    private MeSfcService meSfcService;

    /**
     * 不合格记录
     */
    @GetMapping("/saveNc")
    @ApiOperation(value = "不合格记录")
    public ResponseData save(@RequestParam String sn, @RequestParam String ncBo, @RequestParam BigDecimal doneQty, @RequestParam BigDecimal scrapQty) {


        MeSfc byId = meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo());
        if (byId == null) {
            return ResponseData.error("该标签未维护!");
        }
        if ("已检查".equals(byId.getState())) {
            return ResponseData.error("该标签已检查!");
        }
        meSfcNcLogService.saveNc(byId, ncBo, doneQty, scrapQty);
        return ResponseData.success();
    }


    /**
     * 合格记录
     */
    @GetMapping("/saveOk")
    @ApiOperation(value = "合格记录")
    public ResponseData saveOk(@RequestParam String sn, @RequestParam BigDecimal doneQty, @RequestParam BigDecimal scrapQty) {

        MeSfc byId = meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo());
        if (byId == null) {
            return ResponseData.error("该标签未维护!");
        }
        if ("已检查".equals(byId.getState())) {
            return ResponseData.error("该标签已检查!");
        }
        meSfcNcLogService.saveOk(byId, doneQty, scrapQty);
        return ResponseData.success();
    }
}

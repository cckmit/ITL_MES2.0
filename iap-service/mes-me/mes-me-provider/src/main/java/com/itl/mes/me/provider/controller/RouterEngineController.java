package com.itl.mes.me.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.service.MeSfcService;
import com.itl.mes.me.api.service.RouterEngineService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


/**
 * 工艺路线引擎
 *
 * @author linjl
 * @date 2021-2-4
 */
@RestController
@RequestMapping("/routerEngine")
@Api(tags = "工艺路线引擎")
public class RouterEngineController {
    @Autowired
    private RouterEngineService routerEngineService;

    @Autowired
    private MeSfcService meSfcService;

    /**
     * 执行工艺路线，返回下一工序
     */
    @PostMapping("/executeRouter")
    public ResponseData executeRouter(@RequestBody MeSfc sfc) throws Exception {
        HashMap<String,String> hashMap = routerEngineService.executeRouter(sfc.getBo());
        return ResponseData.success();
    }


}

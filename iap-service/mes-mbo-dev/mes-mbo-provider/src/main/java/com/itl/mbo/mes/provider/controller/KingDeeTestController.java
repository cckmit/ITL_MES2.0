package com.itl.mbo.mes.provider.controller;

import com.itl.mbo.mes.api.entity.WmsDeliveryReturn;
import com.itl.mbo.mes.provider.mapper.WmsDeliveryReturnMapper;
import com.itl.mbo.mes.provider.service.impl.AppAPICallServiceImpl;
import com.itl.mbo.mes.provider.service.impl.RequestHeaderServiceImpl;
import com.itl.mbo.mes.provider.service.impl.WmsDeliveryReturnServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/kingDee")
public class KingDeeTestController {

    @Autowired
    private WmsDeliveryReturnServiceImpl wmsDeliveryReturnService;

    @Autowired
    private WmsDeliveryReturnMapper wmsDeliveryReturnMapper;
    @GetMapping("/test")
    public void test(@RequestParam("id") String id,@RequestParam("type") String type) throws Exception {
        WmsDeliveryReturn wmsDeliveryReturn = wmsDeliveryReturnMapper.selectById(id);
        wmsDeliveryReturnService.ApiBackhaul(wmsDeliveryReturn);
    }

}

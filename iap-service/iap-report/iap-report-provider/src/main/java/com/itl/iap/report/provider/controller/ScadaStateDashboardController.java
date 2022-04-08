package com.itl.iap.report.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.service.ScadaStateDashboardService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @author z
 * @since 2021-09-01
 */
@RestController
@RequestMapping("/ScadaState")
@Api(tags = " 车间scada设备状态看板" )
public class ScadaStateDashboardController {

    private final Logger logger = LoggerFactory.getLogger(ScadaStateDashboardController.class);

    @Autowired
    private ScadaStateDashboardService scadaStateDashboardService;

    @GetMapping("/three")
    public ResponseData<Map<String,String>> getThreeFactoryScadaState() throws CommonException {
        return ResponseData.success(scadaStateDashboardService.getThreeFactoryScadaState());
    }
}

package com.itl.mes.pp.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.schedule.ReceiveRespDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveQueryDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveRespDTO;
import com.itl.mes.pp.api.service.ScheduleReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuchenghao
 * @date 2020/11/16 15:25
 */
@Api("排程管理控制层")
@RestController
@RequestMapping("/p/scheduleReceive")
public class ScheduleReceiveController {


    @Autowired
    ScheduleReceiveService scheduleReceiveService;


    @ApiOperation(value = "receiveList", notes = "查询待接收的排程数据", httpMethod = "POST")
    @PostMapping(value = "/receiveList")
    public ResponseData<IPage<ReceiveRespDTO>> receiveList(@RequestBody ScheduleReceiveQueryDTO scheduleReceiveQueryDTO){

        return ResponseData.success(scheduleReceiveService.receiveList(scheduleReceiveQueryDTO));

    }


    @ApiOperation(value = "dispatch", notes = "排程派工接收", httpMethod = "PUT")
    @PutMapping(value = "/dispatch")
    public ResponseData dispatch(@RequestBody ScheduleReceiveDTO scheduleReceiveDTO) {

        try {
            scheduleReceiveService.dispatch(scheduleReceiveDTO);
            return ResponseData.success(true);
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }


    }



    @ApiOperation(value = "list", notes = "查询派工接收的完成数据和历史数据", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData<IPage<ScheduleReceiveRespDTO>> list(@RequestBody ScheduleReceiveQueryDTO scheduleReceiveQueryDTO){

        return ResponseData.success(scheduleReceiveService.list(scheduleReceiveQueryDTO));

    }


}

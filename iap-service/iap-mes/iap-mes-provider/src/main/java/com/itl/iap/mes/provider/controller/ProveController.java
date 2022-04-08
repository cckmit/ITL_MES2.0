package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.prove.ProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.StationProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.UserProveQueryDTO;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import com.itl.iap.mes.api.service.ProveService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/4 11:40
 */
@RestController
@RequestMapping("/sys/prove")
public class ProveController {


    @Autowired
    ProveService proveService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody ProveQueryDTO proveQueryDTO) {
        return ResponseData.success(proveService.findList(proveQueryDTO));
    }
    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询信息ByState", notes = "分页查询信息ByState")
    public ResponseData findListByState(@RequestBody ProveQueryDTO proveQueryDTO) {
        return ResponseData.success(proveService.findListByState(proveQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ProveEntity proveEntity) {
        proveService.save(proveEntity);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        proveService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(proveService.findById(id));
    }


    @PostMapping("/queryUserProveList")
    @ApiOperation(value = "用户证明分页查询", notes = "用户证明分页查询")
    public ResponseData findUserProveList(@RequestBody UserProveQueryDTO userProveQueryDTO) {
        return ResponseData.success(proveService.findUserProveList(userProveQueryDTO));
    }


    @PostMapping("/queryNotUserProveList")
    @ApiOperation(value = "用户未拥有证明分页查询", notes = "用户未拥有证明分页查询")
    public ResponseData findNotUserProveList(@RequestBody UserProveQueryDTO userProveQueryDTO) {
        return ResponseData.success(proveService.findNotUserProveList(userProveQueryDTO));
    }

    @PostMapping("/queryStationProveList")
    @ApiOperation(value = "工位拥有的证明分页查询", notes = "工位拥有的证明分页查询")
    public ResponseData findStationProveList(@RequestBody StationProveQueryDTO stationProveQueryDTO) {
        return ResponseData.success(proveService.findStationProveList(stationProveQueryDTO));
    }

    @PostMapping("/queryNotStationProveList")
    @ApiOperation(value = "工位未拥有的证明分页查询", notes = "工位未拥有的证明分页查询")
    public ResponseData findNotStationProveList(@RequestBody StationProveQueryDTO stationProveQueryDTO) {
        return ResponseData.success(proveService.findNotStationProveList(stationProveQueryDTO));
    }


}

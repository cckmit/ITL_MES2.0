package com.itl.iap.mes.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.SfcDto;
import com.itl.iap.mes.api.dto.label.LabelQueryDTO;
import com.itl.iap.mes.api.dto.label.LabelSaveDto;
import com.itl.iap.mes.provider.service.impl.LabelServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:08
 */
@RestController
@RequestMapping("/sys/label")
public class LabelController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    LabelServiceImpl labelService;


    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData findList(@RequestBody LabelQueryDTO labelQueryDTO) {
        return ResponseData.success(labelService.findList(labelQueryDTO));
    }

    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询信息ByState", notes = "分页查询信息ByState")
    public ResponseData findListByState(@RequestBody LabelQueryDTO labelQueryDTO) {
        return ResponseData.success(labelService.findListByState(labelQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody LabelSaveDto labelSaveDto) throws CommonException {
        labelService.save(labelSaveDto);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        labelService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData getById(@PathVariable String id) {
        return ResponseData.success(labelService.findById(id));
    }

    @ApiOperation(value = "preview", notes = "预览", httpMethod = "GET")
    @GetMapping(value = "/preview")
    public ResponseData preview(@RequestParam("params") String params, HttpServletResponse response) throws Exception{
        String decode = URLDecoder.decode(params, "UTF-8");
        Map map = JSONObject.parseObject(decode, Map.class);
        return ResponseData.success(labelService.preview(map,response));
    }

    @ApiOperation(value = "exportFile", notes = "导出", httpMethod = "GET")
    @GetMapping(value = "/exportFile")
    public ResponseData exportFile(@RequestParam("params") String params, HttpServletResponse response) throws Exception{
        String decode = URLDecoder.decode(params, "UTF-8");
        Map map = JSONObject.parseObject(decode, Map.class);
        return ResponseData.success(labelService.exportFile(map,response));
    }


    /*
    批量打印接口
     */
    @ApiOperation(value = "batchPrint", notes = "批量打印", httpMethod = "POST")
    @PostMapping(value = "/batchPrint")
    public ResponseData batchPrint(@RequestBody List<Map<String, Object>> list, @RequestParam("labelId") String labelId) {
       // labelService.batchPrint(list, labelId);
        return ResponseData.success(true);
    }

    @GetMapping("/queryFields")
    @ApiOperation(value = "根据表名查询对应的所有字段")
    @ApiImplicitParam(name = "tableName", value = "表名", dataType = "String", paramType = "query")
    public ResponseData<List<String>> queryBytable(@RequestParam("tableName") String tableName)  throws CommonException{

        return ResponseData.success(labelService.selectFieldByTableName(tableName));
    }

    /**
     *
     * @param sfcDto
     * @return
     */
    @PostMapping(value = "/sfcPrint")
    @ApiOperation(value = "条码打印")
    public ResponseData<List<String>> sfcPrint(@RequestBody SfcDto sfcDto) {

        return ResponseData.success( labelService.sfcPrint(sfcDto.getSfc(), sfcDto.getLabelId()));
    }

}

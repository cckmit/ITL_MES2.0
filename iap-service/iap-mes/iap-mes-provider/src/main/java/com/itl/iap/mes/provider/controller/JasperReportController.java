package com.itl.iap.mes.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.provider.report.JasperReport;
import com.itl.iap.mes.provider.utils.MapUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/*
jasper打印类
xuejh
 */
@RestController
@RequestMapping("/m/report")
public class JasperReportController extends JasperReport {

    @Value("${file.path}")
    private String basePath;

    /*
    http://localhost:19000/api/mes/m/report/createPdf?params=filePath:test,name:小明
    传参格式  filePath  必输
    其他属性  为jasper模板的参数
    如果需要传图片 需定义参数 key：convertImg_模板中参数名 value：图片地址  格式aaa.png
     */
    @GetMapping("/createPdfStream")
    @ApiOperation(value = "直接调用该类生成pdf文件流返回到前端", notes = "直接调用该类生成pdf文件流返回到前端")
    public ResponseData getPdfStream(HttpServletResponse response, @RequestParam("params") String params) {
      //  JSONObject.   JREmptyDataSource
        Map<String, Object> map = MapUtils.getStringToMap(params);
        Resource resource = new ClassPathResource("templates/" + map.get("filePath").toString() + ".jasper");
        createPdfStream(response,resource,map);
        return ResponseData.success(true);
    }

    @PostMapping("/createPdfFile")
    @ApiOperation(value = "直接调用该类生成pdf文件 pdfPath文件路劲", notes = "直接调用该类生成pdf文件 pdfPath文件路劲")
    public ResponseData createPdfFile(@RequestBody Map<String, Object> params) {
        Resource resource = new ClassPathResource("templates/" + params.get("filePath").toString() + ".jasper");
        createPdf(resource,params,basePath+params.get("pdfPath").toString());
        return ResponseData.success(true);
    }


}
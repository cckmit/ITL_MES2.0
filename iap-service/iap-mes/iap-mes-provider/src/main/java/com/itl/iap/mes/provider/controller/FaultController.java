package com.itl.iap.mes.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.FaultDto;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.provider.service.impl.FaultServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping("/m/repair/fault")
public class FaultController extends BaseController {

    @Autowired
    private FaultServiceImpl faultService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData getUserRoleIds(@RequestBody FaultDto faultDto) {
       return ResponseData.success(faultService.pageQuery(faultDto));
    }
    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询信息ByState", notes = "分页查询信息ByState")
    public ResponseData getUserRoleIdsByState(@RequestBody FaultDto faultDto) {
        return ResponseData.success(faultService.pageQueryByState(faultDto));
    }
    @PostMapping("/queryType")
    @ApiOperation(value = "查询类型信息", notes = "查询类型信息")
    public ResponseData getType() {
        return ResponseData.success(faultService.list(new QueryWrapper<Fault>().lambda().select(Fault::getType).groupBy(Fault::getType)));
    }
    @PostMapping("/queryByType")
    @ApiOperation(value = "根据类型查询信息", notes = "根据类型查询信息")
    public ResponseData queryByType(@RequestParam String type) {
        return ResponseData.success(faultService.list(new QueryWrapper<Fault>().lambda().eq(Fault::getType,type)));
    }
    @ApiOperation(value = "save", notes = "新增", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody Fault fault) {
        faultService.saveFault(fault);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids) {
        faultService.delete(ids);
        return ResponseData.success(true);
    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{code}")
    public ResponseData getById(@PathVariable String code) {
        return ResponseData.success(faultService.findById(code));
    }

    @ApiOperation(value = "export", notes = "导出模板", httpMethod = "GET")
    @GetMapping(value = "/export")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        faultService.export(request,response);
    }

    @ApiOperation(value = "importFile", notes = "导入", httpMethod = "POST")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) {
        faultService.importFile(file);
        return ResponseData.success(true);    }


    @ApiOperation(value = "download", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/download")
    public String download(HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = (new ClassPathResource("templates/fault.xls")).getInputStream();
            out = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=设备故障导入模板.xls");
            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var15) {
                    ;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var14) {
                }
            }
        }

        return "";
    }


}
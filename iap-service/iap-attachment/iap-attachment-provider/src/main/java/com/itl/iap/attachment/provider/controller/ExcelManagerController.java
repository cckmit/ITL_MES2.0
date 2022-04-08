package com.itl.iap.attachment.provider.controller;

import com.itl.iap.attachment.api.service.IapExcelService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Excel导入导出controller
 *
 * @author 马家伦
 * @date 2020-08-25
 * @since jdk1.8
 */
@Api("Excel导入导出")
@RestController
@Slf4j
@RequestMapping("/excelManager")
public class ExcelManagerController {

    @Resource
    private IapExcelService iapExcelService;

    @GetMapping("/exportOrganizationExcel")
    @ApiOperation(value = "通过EasyPoi下载组织Excel", notes = "通过EasyPoi下载组织Excel")
    public void exportOrganizationExcel(HttpServletResponse response) throws CommonException{
       iapExcelService.exportOrganizationExcel(response);
    }

    @PostMapping("/importOrganizationExcel")
    @ResponseBody
    @ApiOperation(value = "通过EasyPoi上传组织Excel", notes = "通过EasyPoi上传组织Excel")
    public ResponseData<Boolean> importOrganizationExcel(@RequestParam MultipartFile file) throws CommonException {
       return ResponseData.success(iapExcelService.importOrganizationExcel(file));
    }

}

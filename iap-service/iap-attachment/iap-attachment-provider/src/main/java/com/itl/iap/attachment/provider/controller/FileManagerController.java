package com.itl.iap.attachment.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.attachment.api.service.IapUploadFileService;
import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件上传controller
 *
 * @author 谭强
 * @date 2020-08-20
 * @since jdk1.8
 */
@Api("文件上传")
@RestController
@RequestMapping("/fileManager")
public class FileManagerController {

    @Autowired
    private IapUploadFileService iapUploadFileService;

    @ApiOperation(value = "展示文件列表", notes = "展示文件列表")
    @PostMapping("/listFile")
    public IPage<IapUploadFileDto> list(@RequestBody IapUploadFileDto iapUploadFileDto) throws ServletException {
        return iapUploadFileService.pageQuery(iapUploadFileDto);
    }

    @ApiOperation(value = "多个文件上传", notes = "多个文件上传")
    @PostMapping("/uploadFileList")
    public ResponseData<List<IapUploadFile>> uploadFileList(@RequestParam(value = "businessId") String businessId, HttpServletRequest request) {
        return iapUploadFileService.uploadFileList(businessId, request);
    }

    @ApiOperation(value = "单个文件上传", notes = "单个文件上传")
    @PostMapping("/uploadFile")
    public ResponseData<IapUploadFile> uploadFile(@RequestParam(value = "businessId") String businessId, @RequestPart("file") MultipartFile file) {
        return ResponseData.success(iapUploadFileService.uploadFile(businessId, file));
    }

    @ApiOperation(value = "文件下载/预览", notes = "文件下载/预览")
    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, @RequestParam("fileId") String fileId) throws IOException {
        iapUploadFileService.downloadFile(response, fileId);
    }

    @ApiOperation(value = "远程服务调用文件下载/预览", notes = "远程服务调用文件下载/预览")
    @GetMapping("/feignDownloadFile")
    public ResponseData<IapUploadFile> feignDownloadFile(@RequestParam("fileId") String fileId) {
        return ResponseData.success(iapUploadFileService.feignDownloadFile(fileId));
    }

    @ApiOperation(value = "删除文件或目录", notes = "删除文件或目录")
    @PostMapping("/removeFile")
    public ResponseData removeFile(@RequestBody List<IapUploadFile> iapUploadFile) {
        return iapUploadFileService.removeFile(iapUploadFile);
    }

    @ApiOperation(value = "复制文件", notes = "复制文件")
    @PostMapping("/copyFile")
    public ResponseData copyFile(@RequestBody IapUploadFile newIapFile) {
        return iapUploadFileService.copyFile(newIapFile);
    }

    @ApiOperation(value = "重命名文件", notes = "重命名文件")
    @PostMapping("/renameFile")
    public ResponseData renameFile(@RequestBody IapUploadFile iapUploadFile) {
        return ResponseData.success(iapUploadFileService.renameFile(iapUploadFile));
    }

}

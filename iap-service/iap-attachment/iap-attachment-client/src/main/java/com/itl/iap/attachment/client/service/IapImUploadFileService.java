package com.itl.iap.attachment.client.service;

import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.attachment.client.config.AttachmentFallBackConfig;
import com.itl.iap.attachment.client.service.impl.IapImUploadFileServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * TODO
 *
 * @author 汤俊
 * @date 2020-6-30 10:48
 * @since 1.0.0
 */
@FeignClient(value = "iap-attachment-provider", fallback = IapImUploadFileServiceImpl.class, configuration = AttachmentFallBackConfig.class)
public interface IapImUploadFileService {

    @PostMapping("/fileManager/uploadFileList")
    @ApiOperation(value = "文件上传", notes = "文件或图片多个上传")
    ResponseData<List<IapUploadFileDto>> uploadFileList(@RequestParam("businessId") String businessId, HttpServletRequest request);

    @GetMapping("/fileManager/feignDownloadFile")
    @ApiOperation(value = "文件上传", notes = "文件或图片下载")
    ResponseData<IapUploadFile> feignDownloadFile(@RequestParam("fileId") String fileId);

    @PostMapping(value = "/fileManager/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "文件上传", notes = "文件或图片单个上传")
    ResponseData<IapUploadFileDto> uploadFile(@RequestParam("businessId") String businessId, @RequestPart("file") MultipartFile file);

}

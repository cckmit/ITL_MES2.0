/*
 * Copyright ? 2017 海通安恒科技有限公司.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.htah.com.cn/                            *
 *                                                                                     *
 ***************************************************************************************
 */
package com.itl.im.provider.controller;

import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.attachment.client.service.IapImUploadFileService;
import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * IM聊天文件Controller
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
@Api("IM-聊天文件控制层")
@RestController
@RequestMapping("/iapFileMessage")
public class IapImFileController {
    @Resource
    private IapImUploadFileService iapImUploadFileService;

    /**
     * 上传聊天文件
     *
     * @param businessId
     * @param file
     * @return
     */
    @PostMapping(value = "/file")
    @ApiOperation(value = "上传聊天文件", notes = "上传聊天文件")
    public ResponseData<IapUploadFileDto> file(@RequestParam("businessId") String businessId, @RequestPart("file") MultipartFile file) {
        IapUploadFileDto fileData = iapImUploadFileService.uploadFile(businessId, file).getData();
        return ResponseData.success(fileData);
    }

    /**
     * 下载聊天文件
     *
     * @param response
     * @param fileId
     * @throws IOException
     */
    @GetMapping(value = "/downloadFile")
    @ApiOperation(value = "下载聊天文件", notes = "下载聊天文件")
    public void downloadFile(HttpServletResponse response, @RequestParam("fileId") String fileId) throws IOException {
        IapUploadFile iapUploadFile = iapImUploadFileService.feignDownloadFile(fileId).getData();
        if (iapUploadFile != null && iapUploadFile.getBytes() != null) {
            response.reset();
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(iapUploadFile.getFileName() + iapUploadFile.getFileType(), "UTF-8")));
            response.setContentType("application/octet-stream");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(iapUploadFile.getBytes());
            // 写完以后关闭文件流
            toClient.flush();
            toClient.close();
        }
    }
}

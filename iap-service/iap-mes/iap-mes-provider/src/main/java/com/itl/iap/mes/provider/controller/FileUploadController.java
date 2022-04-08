package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.provider.service.impl.FileUploadServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;


@RestController
@RequestMapping("/m/file")
public class FileUploadController extends BaseController {

    @Autowired
    private FileUploadServiceImpl fileUploadService;

    @PostMapping("uploadToServer")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public ResponseData upload(@RequestParam("file") List<MultipartFile> file) {
        return ResponseData.success(fileUploadService.savefile(file));
    }

    @GetMapping("delFile/{id}")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    public ResponseData delFile(@PathVariable("id") String id) {
        fileUploadService.delFile(id);
        return ResponseData.success(true);
    }

    @GetMapping("delFileByFilePath")
    @ApiOperation(value = "通过路径删除文件", notes = "通过路径删除文件")
    public ResponseData delFileByFilePath(@RequestParam("filePath") String filePath) {
        fileUploadService.delFileByFilePath(filePath);
        return ResponseData.success(true);
    }

    @GetMapping("downloadFile/{id}")
    @ApiOperation(value = "下载文件", notes = "下载文件")
    public ResponseData download(HttpServletResponse response, @PathVariable("id") String id) {
        fileUploadService.downLoad(response, id);
        return ResponseData.success(true);
    }

    @GetMapping("/fileReview")
    public String fileReview(HttpServletResponse response,
                             @RequestParam("filePath") String filePath) {

        try {
            FileInputStream file = null;
            OutputStream out = null;
            filePath = URLDecoder.decode(filePath, "UTF-8");
            file = new FileInputStream(filePath);
            int sizi = file.available();
            byte[] data = new byte[sizi];
            file.read(data);
            file.close();
            file = null;
            String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
            response.setContentType("video/" + suffix);
            out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }
}

package com.itl.mes.core.provider.service.impl;

import com.itl.mes.core.provider.exceptionHandler.CommonCode;
import com.itl.mes.core.provider.exceptionHandler.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadServiceImpl {

    @Value("${file.path}")
    private String filePath;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");

    @Transactional
    public String savefile(List<MultipartFile> fileList) {

        fileList.forEach(file -> {
            String str = simpleDateFormat.format(new Date());
            //   String path =  filePath ;

            String path =  filePath + str.split("-")[0] + "/" + str.split("-")[1] + "/" +str.split("-")[2] + "/" + UUID.randomUUID() + "/";

            //第一步：判断文件大小
            if (file.getSize() > 1024 * 1024 * 10) {
                throw new CustomException(CommonCode.FILE_SIZE_BIG);
            }

            File targetFile = new File(path);
            //第二步：判断目录是否存在  不存在：创建目录
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            //第三部：通过输出流将文件写入文件夹并关闭流
            BufferedOutputStream stream = null;
            String fileName = file.getOriginalFilename();

            if (file.isEmpty()) {
                throw new CustomException(CommonCode.FILE_EMPTY);
            } else {
                try {
                    stream = new BufferedOutputStream(new FileOutputStream(path + fileName));
                    stream.write(file.getBytes());
                    stream.flush();
                }catch (Exception e){
                    throw new CustomException(CommonCode.FILE_UPLOAD_FAIL);
                }finally {
                    if (stream != null) try {
                        stream.close();
                    } catch (IOException e) {
                        throw new CustomException(CommonCode.FILE_UPLOAD_FAIL);
                    }
                }
            }
        });
        return "success";
    }
}

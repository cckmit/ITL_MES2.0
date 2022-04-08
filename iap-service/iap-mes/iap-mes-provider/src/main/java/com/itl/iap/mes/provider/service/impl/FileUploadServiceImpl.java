package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.MesFilesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadServiceImpl{

    @Value("${file.path}")
    private String filePath;

    @Autowired
    private MesFilesMapper mesFilesMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");

    @Transactional
    public List<MesFiles> savefile(List<MultipartFile> fileList) {

        List<MesFiles> mesFileList = new ArrayList<>();
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
            MesFiles mesFiles = new MesFiles();
            mesFiles.setFilePath(path + fileName);
            mesFiles.setName(fileName);
            mesFiles.setFileSize(file.getSize());
            mesFilesMapper.insert(mesFiles);
            mesFileList.add(mesFiles);
        });
        return mesFileList;
    }

    public void delFile(String id) {
        mesFilesMapper.deleteById(id);
    }

    public void downLoad(HttpServletResponse response, String id) {
        MesFiles mesFiles = mesFilesMapper.selectById(id);
        downFile(response, mesFiles);
    }

    public void downFile(HttpServletResponse resp, MesFiles mesFiles) {
        resp.setContentType("text/html");
        try {
            FileInputStream in = new FileInputStream(new File(mesFiles.getFilePath()));
           // InputStream in = this.getClass().getResourceAsStream("/template/model.XLSX");
            resp.setContentType("application/octet-stream");
            resp.addHeader("Content-Disposition", "attachment;filename=" + mesFiles.getName());
            OutputStream out = resp.getOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            throw new CustomException(CommonCode.FILE_DOWN_FAIL);
        }

    }

    public void delFileByFilePath(String filePath) {
        mesFilesMapper.delete(new QueryWrapper<MesFiles>().eq("filePath",filePath));
    }
}

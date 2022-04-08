package com.itl.iap.attachment.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.attachment.api.service.IapUploadFileService;
import com.itl.iap.attachment.provider.config.FastDFSClient;
import com.itl.iap.attachment.provider.mapper.IapUploadFileMapper;
import com.itl.iap.attachment.provider.utils.FileUtils;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.itl.iap.attachment.provider.utils.FileUtils.*;

/**
 * 文件上传实现类
 *
 * @author 谭强
 * @date 2020-08-20
 * @since jdk1.8
 */
@Service("iapUploadFileTService")
public class IapUploadFileServiceImpl extends ServiceImpl<IapUploadFileMapper, IapUploadFile> implements IapUploadFileService {

    @Resource
    private IapUploadFileMapper iapUploadFileMapper;
    @Autowired
    private FastDFSClient fastDfsClient;

    /**
     * 分页查询
     *
     * @param iapUploadFileDto 文件上传实例
     * @return IPage<IapUploadFileDto>
     */
    @Override
    public IPage<IapUploadFileDto> pageQuery(IapUploadFileDto iapUploadFileDto) {
        if (null == iapUploadFileDto.getPage()) {
            iapUploadFileDto.setPage(new Page(0, 10));
        }
        return iapUploadFileMapper.pageQuery(iapUploadFileDto.getPage(), iapUploadFileDto);
    }

    /**
     * 文件上传
     *
     * @param businessId 业务id
     * @param request    请求
     * @return ResponseData<List < IapUploadFile>>
     */
    @Override
    public ResponseData<List<IapUploadFile>> uploadFileList(String businessId, HttpServletRequest request) {

        try {
            // Servlet3.0方式上传文件
            Collection<Part> parts = request.getParts();
            List<IapUploadFile> iapUploadFiles = new ArrayList<>();
            parts.forEach(part -> {
                String uuid32 = UUID.uuid32();
                if (part.getContentType() != null) {  // 忽略路径字段,只处理文件类型
                    IapUploadFile iapUploadFile = new IapUploadFile();
                    // 获取文件名
                    String fileName = getFileName(part.getHeader("content-disposition"));
                    // 业务id
                    iapUploadFile.setBusinessId(businessId);
                    // 文件名称
                    iapUploadFile.setFileName(FileUtils.getName(fileName));
                    // 获取文件后缀
                    iapUploadFile.setFileType(getExtension(fileName));
                    // id
                    iapUploadFile.setId(uuid32);
                    // 文件大小
                    iapUploadFile.setFileSize(getFileSize(part.getSize()));
                    // 状态 0 默认开启
                    iapUploadFile.setOnType(IapUploadFileDto.ON_TYPE_0);
                    try {
                        InputStream inputStream = part.getInputStream();
                        String filePath = "";
                        if (FileUtils.imageType(iapUploadFile.getFileType())) {
                            filePath = fastDfsClient.uploadImages(FileUtils.writeToByte(inputStream), fileName);
                        } else {
                            filePath = fastDfsClient.uploadFile(FileUtils.writeToByte(inputStream));
                        }
                        // 文件地址
                        iapUploadFile.setFilePath(filePath);
                        iapUploadFile.setFileUrl(FileUtils.getTrackerUrl() + filePath);
                        iapUploadFiles.add(iapUploadFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.saveBatch(iapUploadFiles);
            return ResponseData.success(iapUploadFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error("err");
        }
    }

    /**
     * 上传单个文件
     *
     * @param businessId 业务id
     * @param file       文件
     * @return IapUploadFile
     */
    @Override
    public IapUploadFile uploadFile(String businessId, MultipartFile file) {
        String uuid32 = UUID.uuid32();
        IapUploadFile iapUploadFile = new IapUploadFile();
        String originalFilename = file.getOriginalFilename();
        // 获取文件名
        String fileName = FileUtils.getName(originalFilename);
        // 业务id
        iapUploadFile.setBusinessId(businessId);
        // 文件名称
        iapUploadFile.setFileName(fileName);
        // 获取文件后缀
        iapUploadFile.setFileType(getExtension(file.getOriginalFilename()));
        // id
        iapUploadFile.setId(uuid32);
        // 文件大小
        iapUploadFile.setFileSize(getFileSize(file.getSize()));
        // 状态 0 默认开启
        iapUploadFile.setOnType(IapUploadFileDto.ON_TYPE_0);
        try {
            String filePath = "";
            if (FileUtils.imageType(iapUploadFile.getFileType())) {
                filePath = fastDfsClient.uploadImages(file.getBytes(), originalFilename);
            } else {
                filePath = fastDfsClient.uploadFile(file.getBytes());
            }
            // 文件地址
            iapUploadFile.setFilePath(filePath);
            iapUploadFile.setFileUrl(FileUtils.getTrackerUrl() + filePath);
            this.save(iapUploadFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return iapUploadFile;
    }

    /**
     * 文件下载/预览
     *
     * @param response 相应体
     * @param id     文件id
     * @throws IOException
     */
    @Override
    public void downloadFile(HttpServletResponse response, String id) throws IOException {
        if (StringUtils.isEmpty(id)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File Is Not Found");
            return;
        }
        IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(id);
        if (null == iapUploadFile || StringUtils.isEmpty(iapUploadFile.getId())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File Is Not Found");
            return;
        }
        response.reset();
        byte[] buffer = fastDfsClient.downloadFile(iapUploadFile.getFilePath());
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(iapUploadFile.getFileName() + iapUploadFile.getFileType(), "UTF-8")));
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        toClient.write(buffer);
        // 写完以后关闭文件流
        toClient.flush();
        toClient.close();
    }

    /**
     * 远程服务调用下载文件
     *
     * @param fileId 文件ID
     * @return IapUploadFile
     */
    @Override
    public IapUploadFile feignDownloadFile(String fileId) {
        if (StringUtils.isEmpty(fileId)) {
            return null;
        }
        IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(fileId);
        if (null == iapUploadFile || StringUtils.isEmpty(iapUploadFile.getId())) {
            return null;
        }
        iapUploadFile.setBytes(fastDfsClient.downloadFile(iapUploadFile.getFilePath()));
        return iapUploadFile;
    }

    /**
     * 删除文件
     *
     * @param iapUploadFile 要删除的文件列表
     * @return ResponseData<Boolean>
     */
    @Override
    public ResponseData<Boolean> removeFile(List<IapUploadFile> iapUploadFile) {
        try {
            // 逻辑删除
            if (!iapUploadFile.isEmpty()) {
                List<String> ids = iapUploadFile.stream().filter(x -> x.getId() != null).map(IapUploadFile::getId).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    iapUploadFileMapper.deleteBatchIds(ids);
                }
                iapUploadFile.forEach(x -> {
                    fastDfsClient.deleteFile(x.getFilePath());
                });
            }
            return ResponseData.success(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error("500");
    }

    /**
     * 重命名文件
     *
     * @param newIapFileName 要修改的文件
     * @return ResponseData
     */
    @Override
    public ResponseData renameFile(IapUploadFile newIapFileName) {
        if (StringUtils.isNotEmpty(newIapFileName.getId()) && StringUtils.isNotEmpty(newIapFileName.getFileName())) {
            try {
                IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(newIapFileName.getId());
                if (iapUploadFile != null) {
                    newIapFileName.getFileName();
                    iapUploadFile.setFileOldName(iapUploadFile.getFileName());
                    iapUploadFile.setFileName(newIapFileName.getFileName());
                    return ResponseData.success(iapUploadFileMapper.updateById(iapUploadFile) != 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseData.error("err");
            }
        }
        return ResponseData.error("err");
    }

    /**
     * 复制文件或目录
     *
     * @param newIapFile 要复制的文件
     * @return ResponseData
     */
    @Override
    public ResponseData copyFile(IapUploadFile newIapFile) {
        try {
            if (StringUtils.isEmpty(newIapFile.getId())) {
                return ResponseData.error("ID Is Not Found");
            }
            IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(newIapFile.getId());
            if (iapUploadFile != null) {
                iapUploadFile.setId(UUID.uuid32());
                File srcFile = new File(iapUploadFile.getFilePath(), iapUploadFile.getFileName());
                File destFile = new File(iapUploadFile.getFilePath(), iapUploadFile.getId());
                FileCopyUtils.copy(srcFile, destFile);
                iapUploadFile.setFileOldName(null);
                if (StringUtils.isNotEmpty(newIapFile.getFileName())) {
                    iapUploadFile.setFileName(newIapFile.getFileName());
                }
                return ResponseData.success(iapUploadFileMapper.insert(iapUploadFile) != 0);
            }
            return ResponseData.error("err");
        } catch (Exception e) {
            return ResponseData.error("err");
        }
    }
}
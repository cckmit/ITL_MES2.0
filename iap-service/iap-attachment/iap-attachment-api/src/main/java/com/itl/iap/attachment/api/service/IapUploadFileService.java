package com.itl.iap.attachment.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.common.base.response.ResponseData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件上传service
 *
 * @author 谭强
 * @date 2020-08-20
 * @since jdk1.8
 */
public interface IapUploadFileService extends IService<IapUploadFile> {

    /**
     * 分页查询
     *
     * @param iapUploadFileDto 文件上传实例
     * @return IPage<IapUploadFileDto>
     */
    IPage<IapUploadFileDto> pageQuery(IapUploadFileDto iapUploadFileDto);

    /**
     * 文件上传
     *
     * @param businessId 业务id
     * @param request    请求
     * @return ResponseData<List < IapUploadFile>>
     */
    ResponseData<List<IapUploadFile>> uploadFileList(String businessId, HttpServletRequest request);

    /**
     * 上传单个文件
     *
     * @param businessId 业务id
     * @param file       文件
     * @return IapUploadFile
     */
    IapUploadFile uploadFile(String businessId, MultipartFile file);

    /**
     * 文件下载/预览
     *
     * @param response 相应体
     * @param path     路径
     * @throws IOException
     */
    void downloadFile(HttpServletResponse response, String path) throws IOException;

    /**
     * 远程服务调用下载文件
     *
     * @param fileId 文件ID
     * @return IapUploadFile
     */
    IapUploadFile feignDownloadFile(String fileId);

    /**
     * 复制文件或目录
     *
     * @param newIapFile 要复制的文件
     * @return ResponseData
     */
    ResponseData copyFile(IapUploadFile newIapFile);

    /**
     * 重命名文件
     *
     * @param newIapFileName 要修改的文件
     * @return ResponseData
     */
    ResponseData renameFile(IapUploadFile newIapFileName);

    /**
     * 删除文件
     *
     * @param iapUploadFile 要删除的文件列表
     * @return ResponseData<Boolean>
     */
    ResponseData<Boolean> removeFile(List<IapUploadFile> iapUploadFile);
}
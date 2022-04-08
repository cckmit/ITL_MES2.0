package com.itl.iap.attachment.client.service.impl;

import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.attachment.client.service.IapImUploadFileService;
import com.itl.iap.common.base.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User fallback
 *
 * @author 汤俊
 * @date 2020-6-30 10:51
 * @since 1.0.0
 */
@Slf4j
@Component
public class IapImUploadFileServiceImpl implements IapImUploadFileService {

    @Override
    public ResponseData<List<IapUploadFileDto>> uploadFileList(String businessId, HttpServletRequest request) {
        log.error("sorry,businessId feign fallback businessId:{}", businessId);
        return ResponseData.error(" feign fallback ");
    }

    @Override
    public ResponseData<IapUploadFile> feignDownloadFile(String fileId) {
        log.error("sorry,fileId feign fallback fileId:{}", fileId);
        return ResponseData.error(" feign fallback ");
    }

    @Override
    public ResponseData<IapUploadFileDto> uploadFile(String businessId, MultipartFile file) {
        log.error("sorry,fileId feign fallback fileId:{}", businessId);
        return ResponseData.error(" feign fallback ");
    }


}

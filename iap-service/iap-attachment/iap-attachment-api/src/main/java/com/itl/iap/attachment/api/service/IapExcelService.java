package com.itl.iap.attachment.api.service;

import com.itl.iap.common.base.exception.CommonException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Excel导入导出Service
 *
 * @author 马家伦
 * @date 2020-08-25
 * @since jdk1.8
 */
public interface IapExcelService {
    /**
     * 导出组织结构Excel
     *
     * @return Boolean
     */
    Boolean exportOrganizationExcel(HttpServletResponse response) throws CommonException;

    /**
     * 导入
     *
     * @param file
     * @return Boolean
     * @throws CommonException
     */
    Boolean importOrganizationExcel(MultipartFile file) throws CommonException;


}

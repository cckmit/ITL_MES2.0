package com.itl.iap.mes.provider.report;


import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.util.Map;

public abstract class BaseReport {
    @Value("${file.path}")
    private String basePath;

    /*
    获取jdbc链接
     */
    abstract Connection getConnection();
    /*
    创建JasperPrint
     */
    JasperPrint createJasperPrint (Resource resource, Map<String, Object> params){
        try {
            convertImgAddressToStream(params);
            FileInputStream fis = new FileInputStream(resource.getFile());
            Connection connection = getConnection();
//            JREmptyDataSource jrEmptyDataSource
            JasperPrint jasperPrint = JasperFillManager.fillReport(fis, params, new JREmptyDataSource());
            return jasperPrint;
        } catch (Exception e) {
            System.out.print(e.getMessage());
            throw new CustomException(CommonCode.JASPER_FAIL);
        }
    };

    /*
    创建pdfStream流
     */
    abstract void createPdfStream(HttpServletResponse response, Resource resource, Map<String, Object> params);


    /*
    创建pdf文件
     */
    abstract void createPdf(Resource resource, Map<String, Object> params, String path);

    /*
    处理map里的imgPath  将图片转成fileInputStream
     */
    public void convertImgAddressToStream(Map<String, Object> params){
        String addKey = "";
        InputStream inputStream = null;

        for (String key : params.keySet()) {
            if(key.startsWith("convertImg_")){
                String imgPath = basePath+params.get(key).toString();
                try {
                    inputStream = new FileInputStream(new File(imgPath));
                    addKey = key.replaceAll("convertImg_","");
                } catch (FileNotFoundException e) {
                    throw new CustomException(CommonCode.IO_FAIL);
                }
            }
        }
        if(StringUtils.isNotBlank(addKey))
            params.put(addKey,inputStream);
    };
}

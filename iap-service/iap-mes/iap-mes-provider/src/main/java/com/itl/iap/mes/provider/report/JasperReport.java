package com.itl.iap.mes.provider.report;


import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.utils.FileUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@Component
public class JasperReport extends BaseReport {
    @Value("${spring.datasource.druid.url}")
    private String url;

    @Value("${spring.datasource.druid.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.druid.username}")
    private String username;

    @Value("${spring.datasource.druid.password}")
    private String password;

    @Override
    public Connection getConnection() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;

        } catch (Exception e) {
            throw new CustomException(CommonCode.CON_FAIL);
        }
    }

    @Override
    protected void createPdfStream(HttpServletResponse response, Resource resource, Map<String, Object> map) {
        JasperPrint jasperPrint = null;
        Connection connection = getConnection();
        Resource resource1 = new ClassPathResource("templates/Blank_A4.jasper");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(resource1.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> params = new HashMap<>();
        try {
            jasperPrint = JasperFillManager.fillReport(fis, params, connection);
            JasperExportManager.exportReportToHtmlFile(jasperPrint,"D:ceshi.html");

        } catch (JRException e) {
            e.printStackTrace();
        }


//        try {
//            JasperPrint jasperPrint = createJasperPrint(resource, map);
//            ServletOutputStream os = response.getOutputStream();
//            JasperExportManager.exportReportToPdfStream(jasperPrint,os);
//        } catch (Exception e) {
//            throw new CustomException(CommonCode.JASPER_FAIL);
//        }
    }



    @Override
    protected void createPdf(Resource resource, Map<String, Object> params, String path) {
        try {
            JasperPrint jasperPrint = createJasperPrint(resource, params);
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
    //        JasperExportManager.exportReportToHtmlFile(jasperPrint,"D:aaa.html");
            FileUtils.writeFile(path,bytes);
        } catch (Exception e) {
            throw new CustomException(CommonCode.JASPER_FAIL);
        }


    }

}

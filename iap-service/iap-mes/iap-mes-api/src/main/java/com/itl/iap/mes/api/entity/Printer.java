package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.Map;
@Getter
@Setter
@TableName("m_printer")
public class Printer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private volatile static Printer instance = null;
    // 私有化构造方法
    private Printer() {

    }

    public static Printer getInstance() {
        if (instance == null) {
            synchronized (Printer.class) {
                if (instance == null) {
                    instance = new Printer();
                }
            }

        }
        return instance;
    }


    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 打印机名称
     */
    @TableField("printerName")
    private String printerName;

    /**
     * 启用状态
     */
    @TableField("state")
    private Integer state;


    public  void defaultPrintPDF(String filePath,String printService){
        try {
            File file = new File(filePath);
            PDDocument document = PDDocument.load(file);
            // 加载成打印文件
            PDFPrintable printable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(specifyPrinter(printService));
            job.setPrintable(printable);
            job.defaultPage();
            job.print();
        }catch (Exception e) {
            e.printStackTrace();
            //  throw new CustomException(CommonCode.PRINT_FAIL);
        }
    }
    public  void sfcPrintPDF(String filePath, String printService, Map<String, Object> params){
        logger.info("进入");
        try {
            File file = new File(filePath);
            PDDocument document = PDDocument.load(file);
            // 加载成打印文件
            PDFPrintable printable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(specifyPrinter(printService));
            job.setPrintable(printable);
            job.defaultPage();
            job.print();
        }catch (Exception e) {
            e.printStackTrace();
            //  throw new CustomException(CommonCode.PRINT_FAIL);
        }
    }


    public  PrintService specifyPrinter(String printerName) {
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(psInFormat, pras);
        if(printService ==null || printService.length==0){
            logger.info("没有找到打印机");
        }
        PrintService myPrinter = null;
        // 遍历所有打印机如果没有选择打印机或找不到该打印机则调用默认打印机
        for (PrintService printService2 : printService) {
            String svcname = printService2.toString();
            if (StringUtils.isNotBlank(printerName) && svcname.contains(printerName)) {
                myPrinter = printService2;
                break;
            }
        }
        if (myPrinter == null) {
            myPrinter = PrintServiceLookup.lookupDefaultPrintService();
        }
        return myPrinter;
    }

}
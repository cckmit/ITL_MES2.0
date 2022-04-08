//package com.itl.iap.mes.provider.utils;
//
//import com.itl.iap.mes.provider.common.CommonCode;
//import com.itl.iap.mes.provider.exception.CustomException;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.printing.PDFPrintable;
//import org.apache.pdfbox.printing.Scaling;
//
//import javax.print.DocFlavor;
//import javax.print.PrintService;
//import javax.print.PrintServiceLookup;
//import javax.print.attribute.HashPrintRequestAttributeSet;
//import java.awt.print.PrinterJob;
//import java.io.File;
//import java.util.Date;
//
//public class PrintUtils {
//
//    public static void defaultPrintPDF(String filePath,String printService){
//        try {
//            File file = new File(filePath);
//            PDDocument document = PDDocument.load(file);
//            // 加载成打印文件
//            PDFPrintable printable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
//            PrinterJob job = PrinterJob.getPrinterJob();
//            job.setPrintService(specifyPrinter(printService));
//            job.setPrintable(printable);
//            job.defaultPage();
//            job.print();
//        }catch (Exception e) {
//            throw new CustomException(CommonCode.PRINT_FAIL);
//        }
//    }
//
//    public static PrintService specifyPrinter(String printerName) {
//        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
//        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//        PrintService printService[] = PrintServiceLookup.lookupPrintServices(psInFormat, pras);
//        PrintService myPrinter = null;
//        // 遍历所有打印机如果没有选择打印机或找不到该打印机则调用默认打印机
//        for (PrintService printService2 : printService) {
//            String svcname = printService2.toString();
//            if (svcname.contains(printerName)) {
//                myPrinter = printService2;
//                break;
//            }
//        }
//        if (myPrinter == null) {
//            myPrinter = PrintServiceLookup.lookupDefaultPrintService();
//        }
//        return myPrinter;
//    }
//
//    public static void main(String[] args) {
//        defaultPrintPDF("D:/7d595512-9d84-4a91-a8a4-39a58de4f7a1.pdf","HP LaserJet MFP M129-M134 PCLmS");
//    }
//    //file:///D:/7d595512-9d84-4a91-a8a4-39a58de4f7a1.pdf
//}

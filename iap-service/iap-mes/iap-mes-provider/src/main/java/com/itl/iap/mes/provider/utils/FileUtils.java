package com.itl.iap.mes.provider.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileUtils {

    public static void writeFile(String path,byte[] bytes){
        int n = 1024;
        FileOutputStream os = null;
        try {
            // 创建文件输出流对象
            File file = new File(path);
            os= new FileOutputStream(file);
            // 写入输出流
            int length = bytes.length;
            int start = 0;
            while(length>=start+n){
                os.write(bytes, start, n);
                start= start+n;
            }
            if(length != start+n){
                n = length-start;
                os.write(bytes, start, n);
            }
        } catch (IOException e) {
        }finally{
            // 关闭输出流
            try {
                if(os !=null){
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }


    public static void createExcel(Object[] headers, List<Map<String, Object>> list, String filePath){
        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("未找到文件");
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("1");
        sheet.setDefaultColumnWidth(20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
//        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();

      //  font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
      //  font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
  //      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//
//        HSSFCellStyle style3 = workbook.createCellStyle();
//        style3.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("leno");

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        // sheet.createFreezePane(7,0,7,0);

        for (short i = 0; i < headers.length; i++){
            HSSFCell cell = row.createCell(i);

            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString((String)headers[i]);
            cell.setCellValue(text);
        }
        for(int n =0 ; n<list.size(); n++){
            row = sheet.createRow(n+1);
            Map<String, Object> one = list.get(n);
            Iterator<Map.Entry<String, Object>> entries = one.entrySet().iterator();
            int i = 0;
            while(entries.hasNext()) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                Map.Entry<String, Object> entry = entries.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                try {
                    String textValue = null;
                    textValue = value==null?"":value.toString();
                    if (textValue != null){
                        cell.setCellValue(String.valueOf(textValue));
                    }
                } catch (Exception  e) {
                    throw new RuntimeException("转文字错误");
                }
                i++;
            }
        }

        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("生成excel错误");
        }
    }


    public static void downExcel(HttpServletResponse response, String filePath) {
        String[] array = filePath.split("/");
        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            response.addHeader("Content-Disposition", "attachment;filename="+filePath.split("/")[array.length - 1]);
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

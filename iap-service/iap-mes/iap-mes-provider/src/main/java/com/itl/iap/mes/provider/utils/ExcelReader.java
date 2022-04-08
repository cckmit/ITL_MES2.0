package com.itl.iap.mes.provider.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.sql.Types.NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.*;

public class ExcelReader {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     * @param inputStream 读取文件的输入流
     * @param fileType 文件后缀名类型（xls或xlsx）
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = new HSSFWorkbook(inputStream);
        return workbook;
    }

    public static List<Map<String, Object>> readExcel2Map(String fileName, InputStream inputStream) {

        Workbook workbook = null;

        try {

            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

            workbook = getWorkbook(inputStream, fileType);

            List<Map<String, Object>> resultDataList = parseExcel2Map(workbook);

            return resultDataList;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (null != workbook) {
                    workbook.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static List<Map<String, Object>> parseExcel2Map(Workbook workbook) {
        List<Map<String, Object>> resultDataList = new ArrayList<>();
        // 解析sheet
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);

            // 校验sheet是否合法
            if (sheet == null) {
                continue;
            }

            // 获取第一行数据
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            if (null == firstRow) {
            }

            // 解析每一行的数据，构造数据对象
            int rowStart = firstRowNum + 1;
            int rowEnd = sheet.getPhysicalNumberOfRows();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row row = sheet.getRow(rowNum);

                if (null == row) {
                    continue;
                }

                Map<String, Object> resultData = convertRowToMap(row);
                if (null == resultData) {
                    continue;
                }
                resultDataList.add(resultData);
            }
        }

        return resultDataList;
    }

    /**
     * 将单元格内容转换为字符串
     * @param cell
     * @return
     */
    private static String convertCellValueToString(Cell cell) {
        if(cell==null){
            return null;
        }
        String returnValue = null;

        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                Double doubleValue = cell.getNumericCellValue();

                // 格式化科学计数法，取一位整数
                DecimalFormat df = new DecimalFormat("0");
                returnValue = df.format(doubleValue);
                break;
            case STRING:    //字符串
                returnValue = cell.getStringCellValue();
                break;

            case BLANK:     // 空值
                break;
            case ERROR:     // 故障
                break;
            default:
                break;
        }
        return returnValue;
    }

    private static Map<String, Object> convertRowToMap(Row row) {
        Map<String, Object> map = new LinkedHashMap<>();

        Cell cell;
        int cellNum = 0;

        cell = row.getCell(cellNum++);
        String code = convertCellValueToString(cell);
        map.put("faultCode", code);

        cell = row.getCell(cellNum++);
        map.put("remark", convertCellValueToString(cell));

        cell = row.getCell(cellNum++);
        map.put("type", convertCellValueToString(cell));

        cell = row.getCell(cellNum++);
        map.put("state", convertCellValueToString(cell));
        cell = row.getCell(cellNum++);
        map.put("repairMethod", convertCellValueToString(cell));

        return map;
    }



//    public static void main(String[] args) {
//
//        InputStream in = null;
//        try {
//             in = new FileInputStream("D:/file/20200320135905timesheet.xls");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        List<Map<String, Object>> readResult = ExcelReader.readExcel2Map("20200320135905timesheet.xls",in);
//
//
//        System.out.print(readResult);
//
//    }
}


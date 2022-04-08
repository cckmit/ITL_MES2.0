package com.itl.iap.report.provider.util;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.report.api.entity.Poi;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;



/**
 * 导出合并单元格工具类
 */
public class ExcelUtils {
    public static void createExcel(String[] title, Map<String/*sheet名*/, List<Map<String/*对应title的值*/, Object>>> maps, int[] mergeIndex, HttpServletResponse response,int orgin,int num) throws CommonException {
        if (title.length==0){
            throw new CommonException("表头不能为空!",30002);
        }
        /*初始化excel模板*/
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        int n = 0;
        /*循环sheet页*/
        for(Map.Entry<String, List<Map<String/*对应title的值*/, Object>>> entry : maps.entrySet()){
            /*实例化sheet对象并且设置sheet名称，book对象*/
            try {
                sheet = workbook.createSheet();
                for(int i=0;i<num+orgin;i++){
                    sheet.setColumnWidth(i, 252*15+323);//width=35
                }
                workbook.setSheetName(n, entry.getKey());
                workbook.setSelectedTab(0);
            }catch (Exception e){
                e.printStackTrace();
            }
            /*初始化head，填值标题行（第一行）*/
            Row row0 = sheet.createRow(0);
            for(int i = 0; i<title.length; i++){
                /*创建单元格，指定类型*/
                Cell cell_1 = row0.createCell(i, CellType.STRING);
                cell_1.setCellValue(title[i]);
            }
            /*得到当前sheet下的数据集合*/
            List<Map<String/*对应title的值*/, Object>> list = entry.getValue();
            /*遍历该数据集合*/
            List<Poi> pois = new ArrayList<>();
            if(null!=workbook){
                Iterator iterator = list.iterator();
                int index = 1;/*这里1是从excel的第二行开始，第一行已经塞入标题了*/
                while (iterator.hasNext()){
                    Row row = sheet.createRow(index);
                    /*取得当前这行的map，该map中以key，value的形式存着这一行值*/
                    Map<String, String> map = (Map<String, String>)iterator.next();
                    /*循环列数，给当前行塞值*/
                    for(int i = 0; i<title.length; i++){
                        String nowContent=map.get(title[i])==null?"":map.get(title[i]);
                        String lastContent="";
                        if(i!=0) {
                            lastContent= map.get(title[i - 1]) == null ? "" : map.get(title[i - 1]);
                        }
                        String old = "";
                        /*old存的是上一行同一位置的单元的值，第一行是最上一行了，所以从第二行开始记*/
                        if(index > 1){
                            old = pois.get(i)==null?"":pois.get(i).getContent();
                        }
                        /*循环需要合并的列*/
                        for(int j = 0; j < mergeIndex.length; j++){
                            if(index == 1){
                                /*记录第一行的开始行和开始列*/
                                Poi poi = new Poi();
                                if(nowContent==null){
                                    poi.setOldContent("");
                                    poi.setContent("");
                                }else {
                                    poi.setOldContent(nowContent);
                                    poi.setContent(nowContent);
                                }
                                poi.setRowIndex(1);
                                poi.setCellIndex(i);
                                pois.add(poi);
                                break;
                            }else if(i > 0 && mergeIndex[j] == i){/*这边i>0也是因为第一列已经是最前一列了，只能从第二列开始*/
                                /*当前同一列的内容与上一行同一列不同时，把那以上的合并, 或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并*/
                                /*如果不需要考虑当前行与上一行内容相同，但是它们的前一列内容不一样则不合并的情况，把下面条件中||pois.get(i).getContent().equals(nowContent) && !pois.get(i - 1).getOldContent().equals(lastContent)去掉就行*/
                                if(!pois.get(i).getContent().equals(nowContent)){
                                    /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                    //CellRangeAddress cra=new CellRangeAddress(pois.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, pois.get(i).getCellIndex()/*从某一列开始*/, pois.get(i).getCellIndex()/*到第几列*/);
                                    //在sheet里增加合并单元格
                                    if(pois.get(i).getRowIndex()!=(index - 1)) {
                                        CellRangeAddress cra=new CellRangeAddress(pois.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, pois.get(i).getCellIndex()/*从某一列开始*/, pois.get(i).getCellIndex()/*到第几列*/);
                                        //在sheet里增加合并单元格
                                        sheet.addMergedRegion(cra);
                                    }
                                    //sheet.addMergedRegion(cra);
                                    /*重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列*/
                                    pois.get(i).setContent(nowContent);
                                    pois.get(i).setRowIndex(index);
                                    pois.get(i).setCellIndex(i);
                                }
                            }
                            /*处理第一列的情况*/
                            if(mergeIndex[j] == i && i == 0 && !pois.get(i).getContent().equals(nowContent) && index>2){
                                /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                CellRangeAddress cra=new CellRangeAddress(pois.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, pois.get(i).getCellIndex()/*从某一列开始*/, pois.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                                /*重新记录该列的内容为当前内容，行标记改为当前行标记*/
                                pois.get(i).setContent(nowContent);
                                pois.get(i).setRowIndex(index);
                                pois.get(i).setCellIndex(i);
                            }

                            /*最后一行没有后续的行与之比较，所有当到最后一行时则直接合并对应列的相同内容*/
                            if(mergeIndex[j] == i && index == list.size() && index>2 && (pois.get(i).getRowIndex() !=index)){
                                CellRangeAddress cra=new CellRangeAddress(pois.get(i).getRowIndex()/*从第二行开始*/, index/*到第几行*/, pois.get(i).getCellIndex()/*从某一列开始*/, pois.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                            }
                        }
                        Cell cell = row.createCell(i, CellType.STRING);
                        XSSFCellStyle style= (XSSFCellStyle) workbook.createCellStyle();
                        style.setAlignment(HorizontalAlignment.CENTER);
                        style.setVerticalAlignment(VerticalAlignment.CENTER);
                        style.setWrapText(true);
                        cell.setCellStyle(style);
                        if(nowContent==null){
                            cell.setCellValue("");
                        }else{
                            cell.setCellValue(nowContent);
                        }
                        /*在每一个单元格处理完成后，把这个单元格内容设置为old内容*/
                        pois.get(i).setOldContent(old);
                    }
                    index++;
                }
            }
            n++;
        }
        downLoadExcel("admin.xls",response,workbook);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws CommonException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new CommonException( e.getMessage(), CommonExceptionDefinition.EXCEI_EXCEPTION);
        }finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

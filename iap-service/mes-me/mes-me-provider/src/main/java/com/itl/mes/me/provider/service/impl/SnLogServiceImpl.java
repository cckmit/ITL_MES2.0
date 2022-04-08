package com.itl.mes.me.provider.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.UUID;
import com.itl.mes.me.api.dto.SnLogDto;
import com.itl.mes.me.api.entity.SnLog;
import com.itl.mes.me.api.service.SnLogService;
import com.itl.mes.me.provider.mapper.SnLogMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author 崔翀赫
 * @date 2020/12/25 14:06
 * @since JDK1.8
 */
@Service("zSnLogService")
public class SnLogServiceImpl extends ServiceImpl<SnLogMapper, SnLog> implements SnLogService {

    @Autowired
    private SnLogMapper snLogMapper;
    @Autowired
    private UserUtil userUtil;


    @Override
    public IPage<SnLog> getAll(SnLogDto snLogDto) {
        if (ObjectUtil.isEmpty(snLogDto.getPage())) {
            snLogDto.setPage(new Page(0, 10));
        }
        return snLogMapper.getAll(snLogDto.getPage(), snLogDto);
    }

    @Override
    public void saveSnLogs(SnLog snLog) {
        if(StringUtils.isEmpty(snLog.getBo())){
            snLog.setBo(UUID.uuid32());
            snLog.setCreateDate(new Date());
            snLog.setCreateUser(userUtil.getUser().getUserName());
            save(snLog);
        }else {
            updateById(snLog);
        }

    }


    @Override
    public void exportOperaton(HttpServletResponse response) {
        Workbook workBook = null;
        try {
            // 获取数据源
            IPage<SnLog> allPage = getAll(new SnLogDto());
            List<SnLog> all = allPage.getRecords();
            // 创建参数对象（用来设定excel得sheet得内容等信息）
            ExportParams stationExportParams = new ExportParams();
            // 设置sheet得名称
            stationExportParams.setSheetName("条码号段生产记录");
            // 创建sheet1使用得map
            Map<String, Object> stationExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            stationExportMap.put("title", stationExportParams);
            // 模版导出对应得实体类型
            stationExportMap.put("entity", SnLog.class);
            // sheet中要填充得数据
            stationExportMap.put("data", all);

            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(stationExportMap);

            // 执行方法
            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            String fileName = "条码号段生产记录.xls";
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader(
                    "Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workBook.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

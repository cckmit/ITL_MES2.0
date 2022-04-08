package com.itl.iap.attachment.provider.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.itl.iap.attachment.api.service.IapExcelService;
import com.itl.iap.attachment.client.service.OrganizationService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.system.api.dto.IapSysOrganizationTDto;
import com.itl.iap.system.api.entity.IapSysOrganizationT;
import com.itl.iap.system.api.entity.excelDataEntity.SysOrganizationExcelEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入导出实现类
 *
 * @author 马家伦
 * @date 2020-08-25
 * @since jdk1.8
 */
@Service
@Slf4j
public class IapExcelServiceImpl implements IapExcelService {

    /**
     * 用于判断需要导出EXCEL数据的大小
     */
    private static final Integer BIG_EXPORT_LIST_SISE = 50000;

    /**
     * 用于判断上传Excel数据的大小
     */
    private static final int BIG_IMPORT_LIST_SIZE = 20000;

    @Resource
    OrganizationService organizationService;

    /**
     * 导出组织结构Excel
     *
     * @return Boolean
     */
    @Override
    public Boolean exportOrganizationExcel(HttpServletResponse response) throws CommonException {
        try {
            // 告诉浏览器用什么软件可以打开此文件
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("组织结构Excel", "UTF-8") + ".xls");
            //编码
            response.setCharacterEncoding("UTF-8");
            // excel总体设置
            ExportParams exportParams = new ExportParams();

            long start = System.currentTimeMillis();

            // 从 system模块中查询组织结构列表
            ResponseData<List<SysOrganizationExcelEntity>> responseData = organizationService.pageQuery(new IapSysOrganizationTDto());
            if (responseData.getData().isEmpty()) {
                throw new CommonException("数据查询错误", 500);
            }
            // 转换 list 对象
            List<SysOrganizationExcelEntity> list = DtoUtils.convertListForExportExcel(SysOrganizationExcelEntity.class, responseData.getData());
            int listSize = list.size();
            if (listSize > BIG_EXPORT_LIST_SISE) {
                // 使用 easypoi 把 list 装入 excel 里面
                Workbook workbook = ExcelExportUtil.exportBigExcel(new ExportParams(), SysOrganizationExcelEntity.class, new IExcelExportServer() {
                    @Override
                    public List<Object> selectListForExcelExport(Object obj, int page) {
                        if (((int) obj) == page) {
                            return null;
                        }
                        List<Object> objList = new ArrayList<Object>();
                        list.stream().forEach(x -> {
                            objList.add(x);
                        });
                        return objList;
                    }
                }, 2);
                workbook.write(response.getOutputStream());
                list.clear();
                workbook.close();
                log.info("执行导出Excel结束，一共用了" + (System.currentTimeMillis() - start) / 1000 + "秒");
                return true;
            } else {
                // 使用 easypoi 把 list 装入 excel 里面
                Workbook workbook = ExcelExportUtil.exportExcel(exportParams, SysOrganizationExcelEntity.class, list);
                workbook.write(response.getOutputStream());
                list.clear();
                workbook.close();
                log.info("执行导出Excel结束，一共用了" + (System.currentTimeMillis() - start) / 1000 + "秒");
                return true;
            }

        } catch (Exception e) {
            throw new CommonException("导出Excel出现错误", 500);
        }
    }

    /**
     * 导入
     *
     * @param file
     * @return Boolean
     * @throws CommonException
     */
    @Override
    public Boolean importOrganizationExcel(MultipartFile file) throws CommonException {
        // 把上传的文件转成流
        try {
            log.info("-->开始读取上传的文件: " + file.getOriginalFilename());
            long start = System.currentTimeMillis();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());

            // 设置导入参数
            ImportParams params = new ImportParams();
            // 开启多线程
            params.setConcurrentTask(true);
            // 使用 easypoi 读取Excel数据并存入 list 中
            List<SysOrganizationExcelEntity> importExcelList = ExcelImportUtil.importExcel(bufferedInputStream, SysOrganizationExcelEntity.class, params);
            log.info("读取Excel数据结束正在执行导入操作，一共用了" + (System.currentTimeMillis() - start) / 1000 + "秒");
            // 关闭文件流
            bufferedInputStream.close();
            // 转换 list 对象
            List<IapSysOrganizationT> sysOrganizationList = DtoUtils.convertListForImportExcel(IapSysOrganizationT.class, importExcelList);
            importExcelList.clear();
            // 如果传入的数组长度大于 BIG_LIST_SIZE，将分批次导入
            int listSize = sysOrganizationList.size();
            log.info("正在将数据导入数据库，list的长度为: " + listSize);
            if (listSize > BIG_IMPORT_LIST_SIZE) {
                int page = (listSize + BIG_IMPORT_LIST_SIZE - 1) / BIG_IMPORT_LIST_SIZE;
                for (int i = 1; i <= page; i++) {
                    if (i == page) {
                        organizationService.saveBatch(sysOrganizationList.subList(BIG_IMPORT_LIST_SIZE * (i - 1), listSize));
                    } else {
                        organizationService.saveBatch(sysOrganizationList.subList(BIG_IMPORT_LIST_SIZE * (i - 1), BIG_IMPORT_LIST_SIZE * i));
                    }
                }
            } else {
                organizationService.saveBatch(sysOrganizationList);
            }
            sysOrganizationList.clear();
            log.info("执行导入Excel结束，一共用了" + (System.currentTimeMillis() - start) / 1000 + "秒");
            return true;
        } catch (Exception e) {
            throw new CommonException("Excel导入失败", 500);
        }
    }
}

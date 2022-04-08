package com.itl.mes.pp.provider.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.pp.api.dto.MachineProductCapacityDto;
import com.itl.mes.pp.api.entity.DeviceItemCapacityEntity;
import com.itl.mes.pp.api.service.DeviceItemCapacityService;
import com.itl.mes.pp.provider.mapper.DeviceItemCapacityMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 崔翀赫
 * @date 2020/12/17 13:35
 * @since JDK1.8
 */
@Service("pMachineProductCapacityService")
public class DeviceItemCapacityServiceImpl
        extends ServiceImpl<DeviceItemCapacityMapper, DeviceItemCapacityEntity>
        implements DeviceItemCapacityService {

    @Autowired
    private DeviceItemCapacityMapper deviceItemCapacityMapper;
    @Autowired
    private UserUtil userUtil;

    @Override
    public IPage<DeviceItemCapacityEntity> getPage(MachineProductCapacityDto machineProductCapacityDto) {
        Page page;
        if (machineProductCapacityDto.getPage() == 0 && machineProductCapacityDto.getLimit() == 0) {
            page = new Page(0, 10);
        } else {
            page = new Page(machineProductCapacityDto.getPage(), machineProductCapacityDto.getLimit());
        }

        long size = page.getSize();
        long current = page.getCurrent()-1;

        List<DeviceItemCapacityEntity> all = deviceItemCapacityMapper.getAll(machineProductCapacityDto,UserUtils.getSite());
        List<DeviceItemCapacityEntity> collect = all.stream().skip(size * current).limit(size).collect(Collectors.toList());
        long total;
        long pages;
        if (CollectionUtils.isEmpty(all)) {
            total = 0L;
            pages = 0L;
        } else {
            total = all.size();
            pages = total / size;
            if (total % size != 0) {
                pages++;
            }
        }

        IPage<DeviceItemCapacityEntity> deviceItemCapacityEntityIPage = new Page<>();
        deviceItemCapacityEntityIPage.setRecords(collect);
        deviceItemCapacityEntityIPage.setPages(pages);
        deviceItemCapacityEntityIPage.setTotal(total);
        deviceItemCapacityEntityIPage.setCurrent(current);
        deviceItemCapacityEntityIPage.setSize(size);

        return deviceItemCapacityEntityIPage;
    }

    @Override
    public DeviceItemCapacityEntity getOneById(String bo) {
        List<DeviceItemCapacityEntity> list = deviceItemCapacityMapper.getOneById(bo);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void saveMachineProductCapacityEntity(
            DeviceItemCapacityEntity deviceItemCapacityEntity) {
        deviceItemCapacityEntity.setWorkShopBo(new WorkShopHandleBO(UserUtils.getSite(), deviceItemCapacityEntity.getWorkShopBo()).getBo());
        deviceItemCapacityEntity.setDeviceBo("RES:dongyin," + deviceItemCapacityEntity.getDevice());
        if (StringUtils.isNotEmpty(deviceItemCapacityEntity.getBo())) {
            deviceItemCapacityEntity.setUpdatedBy(userUtil.getUser().getUserName());
            deviceItemCapacityEntity.setUpdatedTime(new Date());
            deviceItemCapacityMapper.updateById(deviceItemCapacityEntity);
        } else {
            deviceItemCapacityEntity.setSite(UserUtils.getSite());
            deviceItemCapacityEntity.setCreatedBy(userUtil.getUser().getUserName());
            deviceItemCapacityEntity.setCreatedTime(new Date());
            deviceItemCapacityMapper.insert(deviceItemCapacityEntity);
        }


    }

    @Override
    public void exportOperaton(HttpServletResponse response) {
        Workbook workBook = null;
        try {
            // 获取数据源
            List<DeviceItemCapacityEntity> all = deviceItemCapacityMapper.getAll(new MachineProductCapacityDto(),UserUtils.getSite());
            // 创建参数对象（用来设定excel得sheet得内容等信息）
            ExportParams stationExportParams = new ExportParams();
            // 设置sheet得名称
            stationExportParams.setSheetName("机台产品产能");
            // 创建sheet1使用得map
            Map<String, Object> stationExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            stationExportMap.put("title", stationExportParams);
            // 模版导出对应得实体类型
            stationExportMap.put("entity", DeviceItemCapacityEntity.class);
            // sheet中要填充得数据
            stationExportMap.put("data", all);

            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(stationExportMap);

            // 执行方法
            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            String fileName = "机台产品产能表.xls";
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

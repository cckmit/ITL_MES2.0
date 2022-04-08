package com.itl.iap.data.source.service.impl;

import com.itl.iap.data.source.entity.DataSource;
import com.itl.iap.data.source.service.DBChangeService;
import com.itl.iap.data.source.uitls.DBContextHolder;
import com.itl.iap.data.source.uitls.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 切换数据库Service实现类
 *
 * @author tanq
 * @date 2020/6/18 16:34
 * @since jdk1.8
 */
@Service("dBChangeService")
@Slf4j
public class DBChangeServiceImpl implements DBChangeService {

    @Autowired
    private DynamicDataSource dynamicDataSource;

    /**
     * 切换数据源方法
     *
     * @param datasourceCode  需要切换的数据源
     * @param dataSourcesList 所有数据库源列表
     * @return
     */
    @Override
    public boolean changeDb(@NotNull String datasourceCode, @NotNull List<DataSource> dataSourcesList) {
        try {
            //默认切换到主数据源,进行整体资源的查找
            DBContextHolder.clearDataSource();
            if (!dataSourcesList.isEmpty()) {
                for (DataSource dataSource : dataSourcesList) {
                    if (dataSource.getDatasourceCode().equals(datasourceCode)) {
                        log.info("需要使用的的数据源已经找到,datasourceId是：" + dataSource.getDatasourceCode());
                        //创建数据源连接&检查 若存在则不需重新创建
                        dynamicDataSource.createDataSourceWithCheck(dataSource);
                        //切换到该数据源
                        DBContextHolder.setDataSource(dataSource.getDatasourceCode());
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("=============================> 数据源切换失败");
            e.printStackTrace();
        }
        return false;
    }

}

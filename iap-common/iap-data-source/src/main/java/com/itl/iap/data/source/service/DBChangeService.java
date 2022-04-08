package com.itl.iap.data.source.service;

import com.itl.iap.data.source.entity.DataSource;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 切换数据库Service
 *
 * @author tanq
 * @date 2020/6/18 16:34
 * @since jdk1.8
 */
public interface DBChangeService {
    boolean changeDb(@NotNull String datasourceCode, @NotNull List<DataSource> dataSourcesList);
}

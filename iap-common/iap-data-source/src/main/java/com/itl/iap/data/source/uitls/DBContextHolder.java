package com.itl.iap.data.source.uitls;

import lombok.extern.slf4j.Slf4j;

/**
 * 获得和设置上下文环境，主要负责改变上下文数据源
 *
 * @author tanq
 * @date 2020/6/18 15:08
 * @since jdk1.8
 */
@Slf4j
public class DBContextHolder {
    // 对当前线程的操作-线程安全的
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    // 调用此方法，切换数据源
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
        log.info("数据源已切换至：" + dataSource);
    }

    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }

    // 删除数据源
    public static void clearDataSource() {
        contextHolder.remove();
        log.info("================数据源切换回默认数据源");
    }
}

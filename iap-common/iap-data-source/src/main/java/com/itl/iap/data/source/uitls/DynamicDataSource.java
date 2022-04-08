package com.itl.iap.data.source.uitls;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import com.itl.iap.data.source.entity.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Set;

/**
 * 动态获取数据源类
 *
 * @author tanq
 * @date 2020/6/18 19:41
 * @since jdk1.8
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private boolean debug = true;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Object, Object> dynamicTargetDataSources;
    private Object dynamicDefaultTargetDataSource;


    /**
     * 获取当前数据源方法
     *
     * @return String 数据源名称
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String datasource = DBContextHolder.getDataSource();
        if (!StringUtils.isEmpty(datasource)) {
            Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
            if (dynamicTargetDataSources2.containsKey(datasource)) {
                log.info("---当前数据源：" + datasource + "---");
            } else {
                log.info("不存在的数据源：");
                return null;
//                    throw new ADIException("不存在的数据源："+datasource,500);
            }
        } else {
            log.info("---当前数据源：默认数据源---");
        }
        return datasource;
    }

    /**
     * 设置数据源
     *
     * @param targetDataSources 目标数据源
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {

        super.setTargetDataSources(targetDataSources);

        this.dynamicTargetDataSources = targetDataSources;

    }


    /**
     * 创建数据源
     *
     * @param key          名称
     * @param driveClass   数据库类名
     * @param url          数据库连接地址
     * @param username     数据库用户名
     * @param password     数据库密码
     * @param databasetype 用于区分数据库（MySQL/Oracle）
     * @return
     */
    public boolean createDataSource(String key, String driveClass, String url, String username, String password, String databasetype) {
        try {
            // 排除连接不上的错误
            try {
                Class.forName(driveClass);
                // 相当于连接数据库
                DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                return false;
            }
            // HikariDataSource druidDataSource = new HikariDataSource();
            @SuppressWarnings("resource")
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setName(key);
            druidDataSource.setDriverClassName(driveClass);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            //初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
            druidDataSource.setInitialSize(1);
            //最大连接池数量
            druidDataSource.setMaxActive(20);
            //获取连接时最大等待时间，单位毫秒。当链接数已经达到了最大链接数的时候，应用如果还要获取链接就会出现等待的现象，等待链接释放并回到链接池，如果等待的时间过长就应该踢掉这个等待，不然应用很可能出现雪崩现象
            druidDataSource.setMaxWait(60000);
            //最小连接池数量
            druidDataSource.setMinIdle(5);
            String validationQuery = "";
            // 区分数据库
            if (DBUtil.MYSQL.equalsIgnoreCase(databasetype)) {
                // driveClass = DBUtil.MYSQL_DRIVER;
                validationQuery = "select 1 from dual";
            } else if (DBUtil.ORACLE.equalsIgnoreCase(databasetype)) {
                // driveClass = DBUtil.ORACLE_DRIVER;
                //是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭
                druidDataSource.setPoolPreparedStatements(true);
                druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(50);
                // 超时默认设置为60000毫秒
                int sqlQueryTimeout = DBUtil.ORACLE_READ_TIME_OUT;
                //对于耗时长的查询sql，会受限于ReadTimeout的控制，单位毫秒
                druidDataSource.setConnectionProperties("oracle.net.CONNECT_TIMEOUT=6000;oracle.jdbc.ReadTimeout=" + sqlQueryTimeout);
            } else {
                DBContextHolder.clearDataSource();
                throw new RuntimeException("驱动加载不存在");
            }
//            else if("sqlserver2000".equalsIgnoreCase(databasetype)){
//                driveClass = DBUtil.sql2000driver;
//                validationQuery = "select 1";
//            } else if("sqlserver".equalsIgnoreCase(databasetype)){
//                driveClass = DBUtil.sql2005driver;
//                validationQuery = "select 1";
//            }
            //申请连接时执行validationQuery检测连接是否有效，这里建议配置为TRUE，防止取到的连接不可用
            druidDataSource.setTestOnBorrow(true);
            //建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
            druidDataSource.setTestWhileIdle(true);
            //用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
            druidDataSource.setValidationQuery(validationQuery);
            //属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall
            druidDataSource.setFilters("stat");
            //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
            //配置一个连接在池中最小生存的时间，单位是毫秒，这里配置为3分钟180000
            druidDataSource.setMinEvictableIdleTimeMillis(180000);
            //打开druid.keepAlive之后，当连接池空闲时，池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作，即执行druid.validationQuery指定的查询SQL，一般为select * from dual，只要minEvictableIdleTimeMillis设置的小于防火墙切断连接时间，就可以保证当连接空闲时自动做保活检测，不会被防火墙切断
            druidDataSource.setKeepAlive(true);
            //是否移除泄露的连接/超过时间限制是否回收
            druidDataSource.setRemoveAbandoned(true);
            //泄露连接的定义时间(要超过最大事务的处理时间)；单位为秒。这里配置为1小时
            druidDataSource.setRemoveAbandonedTimeout(3600);
            //移除泄露连接发生是是否记录日志
            druidDataSource.setLogAbandoned(true);
            druidDataSource.init();
            this.dynamicTargetDataSources.put(key, druidDataSource);
            // 将map赋值给父类的TargetDataSources
            setTargetDataSources(this.dynamicTargetDataSources);
            // 将TargetDataSources中的连接信息放入resolvedDataSources管理
            super.afterPropertiesSet();
            log.info(key + "数据源初始化成功");
            //log.info(key+"数据源的概况："+druidDataSource.dump());
            return true;
        } catch (Exception e) {
            log.error(e + "");
            return false;
        }
    }

    /**
     * 删除数据源
     *
     * @param datasourceid
     * @return
     */
    public boolean delDatasources(String datasourceid) {
        Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
        if (dynamicTargetDataSources2.containsKey(datasourceid)) {
            Set<DruidDataSource> druidDataSourceInstances = DruidDataSourceStatManager.getDruidDataSourceInstances();
            for (DruidDataSource l : druidDataSourceInstances) {
                if (datasourceid.equals(l.getName())) {
                    dynamicTargetDataSources2.remove(datasourceid);
                    DruidDataSourceStatManager.removeDataSource(l);
                    // 将map赋值给父类的TargetDataSources
                    setTargetDataSources(dynamicTargetDataSources2);
                    // 将TargetDataSources中的连接信息放入resolvedDataSources管理
                    super.afterPropertiesSet();
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 测试数据源连接是否有效
     *
     * @param key
     * @param driveClass 数据库类名
     * @param url        数据库连接地址
     * @param username   数据库用户名
     * @param password   数据库密码
     * @return
     */
    public boolean testDatasource(String key, String driveClass, String url, String username, String password) {
        try {
            Class.forName(driveClass);
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置默认数据源
     *
     * @param defaultTargetDataSource 默认数据源对象
     */
    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        this.dynamicDefaultTargetDataSource = defaultTargetDataSource;
    }

    /**
     * 创建带有检查的数据源
     *
     * @param dataSource 数据源
     * @throws Exception
     */
    public void createDataSourceWithCheck(DataSource dataSource) throws Exception {
        String datasourceId = dataSource.getDatasourceCode();
        log.info("正在检查数据源：" + datasourceId);
        Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
        if (dynamicTargetDataSources2.containsKey(datasourceId)) {
            log.info("数据源" + datasourceId + "之前已经创建，准备测试数据源是否正常...");
            //DataSource druidDataSource = (DataSource) dynamicTargetDataSources2.get(datasourceId);
            DruidDataSource druidDataSource = (DruidDataSource) dynamicTargetDataSources2.get(datasourceId);
            boolean rightFlag = true;
            Connection connection = null;
            try {
                log.info(datasourceId + "数据源的概况->当前闲置连接数：" + druidDataSource.getPoolingCount());
                long activeCount = druidDataSource.getActiveCount();
                log.info(datasourceId + "数据源的概况->当前活动连接数：" + activeCount);
                if (activeCount > 0) {
                    log.info(datasourceId + "数据源的概况->活跃连接堆栈信息：" + druidDataSource.getActiveConnectionStackTrace());
                }
                log.info("准备获取数据库连接...");
                connection = druidDataSource.getConnection();
                log.info("数据源" + datasourceId + "正常");
            } catch (Exception e) {
                // 把异常信息打印到日志文件
                log.error(e.getMessage(), e);
                rightFlag = false;
                log.info("缓存数据源" + datasourceId + "已失效，准备删除...");
                if (delDatasources(datasourceId)) {
                    log.info("缓存数据源删除成功");
                } else {
                    log.info("缓存数据源删除失败");
                }
            } finally {
                if (null != connection) {
                    connection.close();
                }
            }
            if (rightFlag) {
                log.info("不需要重新创建数据源");
                return;
            } else {
                log.info("准备重新创建数据源...");
                createDataSource(dataSource);
                log.info("重新创建数据源完成");
            }
        } else {
            createDataSource(dataSource);
        }
    }

    /**
     * 创建数据源
     *
     * @param dataSource 数据源
     * @throws Exception
     */
    private void createDataSource(DataSource dataSource) throws Exception {
        String datasourceId = dataSource.getDatasourceCode();
        log.info("准备创建数据源" + datasourceId);
        String databaseType = dataSource.getDatabaseType();
        String username = dataSource.getUserName();
        String password = dataSource.getPassWord();
        String url = dataSource.getDatabaseUrl();
        String driveClass = "";
        if (DBUtil.MYSQL.equalsIgnoreCase(databaseType)) {
            driveClass = DBUtil.MYSQL_DRIVER;
        } else if (DBUtil.ORACLE.equalsIgnoreCase(databaseType)) {
            driveClass = DBUtil.ORACLE_DRIVER;
        } else {
            DBContextHolder.clearDataSource();
            log.error("驱动加载不存在");
            throw new RuntimeException("驱动加载不存在");
        }/*else if("sqlserver2000".equalsIgnoreCase(databasetype)){
            driveClass = DBUtil.sql2000driver;
        } else if("sqlserver".equalsIgnoreCase(databasetype)){
            driveClass = DBUtil.sql2005driver;
        }*/
        if (testDatasource(datasourceId, driveClass, url, username, password)) {
            boolean result = this.createDataSource(datasourceId, driveClass, url, username, password, databaseType);
            if (!result) {
                log.error("数据源" + datasourceId + "配置正确，但是创建失败");
                DBContextHolder.clearDataSource();
                throw new RuntimeException("数据源" + datasourceId + "配置正确，但是创建失败");
            }
        } else {
            log.error("数据源配置有错误");
            DBContextHolder.clearDataSource();
            throw new RuntimeException("数据源配置有错误");
        }
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @return the dynamicTargetDataSources
     */
    public Map<Object, Object> getDynamicTargetDataSources() {
        return dynamicTargetDataSources;
    }

    /**
     * @param dynamicTargetDataSources the dynamicTargetDataSources to set
     */
    public void setDynamicTargetDataSources(Map<Object, Object> dynamicTargetDataSources) {
        this.dynamicTargetDataSources = dynamicTargetDataSources;
    }

    /**
     * @return the dynamicDefaultTargetDataSource
     */
    public Object getDynamicDefaultTargetDataSource() {
        return dynamicDefaultTargetDataSource;
    }

    /**
     * @param dynamicDefaultTargetDataSource the dynamicDefaultTargetDataSource to set
     */
    public void setDynamicDefaultTargetDataSource(Object dynamicDefaultTargetDataSource) {
        this.dynamicDefaultTargetDataSource = dynamicDefaultTargetDataSource;
    }

}

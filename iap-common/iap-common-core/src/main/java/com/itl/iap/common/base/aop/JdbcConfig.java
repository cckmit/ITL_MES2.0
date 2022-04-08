package com.itl.iap.common.base.aop;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.itl.iap.common.base.dto.IapOpsLogTDto;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * jdbc配置类
 *
 * @author LiShuaiPeng
 * @date 2020/5/25 9:40
 * @since JDK 1.8
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class JdbcConfig {

    /**
     * 数据库地址
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 驱动名称
     */
    private String driverClassName;

    /**
     * 获取数据库连接
     *
     * @return conn
     */
    public Connection getConnection() throws Exception {
        if (StringUtils.isNotEmpty(url) && !this.getUrl().isEmpty() && StringUtils.isNotEmpty(username) && !this.getUsername().isEmpty() && !this.getPassword().isEmpty()
                && !this.getDriverClassName().isEmpty()) {
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
            }
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(this.getUrl(), this.getUsername(), this.getPassword());
            } catch (SQLException e) {
            }
            return conn;
        } else {
            return null;
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param connection
     * @param resultSet
     * @param preparedStatement
     */
    private void closeConnection(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * 保存接口列表到数据库（iap_sys_api_t）表
     *
     * @param iapSysApiList 接口列表
     * @return Integer 接口列表的大小
     * @throws Exception
     */
    public Integer insertList(List<IapSysApiT> iapSysApiList) throws Exception {

        // 获取数据库连接
        Connection connection = this.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO iap_sys_api_t(id,system_code,model_name,class_name,class_desc,class_url,method_name,method_desc" +
                    ",method_url,request_type,enabled,last_update_by,last_update_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
            ps = connection.prepareStatement(sql);
            Timestamp date = this.getSqlDate();
            for (int i = 0; i < iapSysApiList.size(); i++) {
                ps.setString(1, this.uuid32());
                ps.setString(2, iapSysApiList.get(i).getSystemCode());
                ps.setString(3, iapSysApiList.get(i).getModelName());
                ps.setString(4, iapSysApiList.get(i).getClassName());
                ps.setString(5, iapSysApiList.get(i).getClassDesc());
                ps.setString(6, iapSysApiList.get(i).getClassUrl());
                ps.setString(7, iapSysApiList.get(i).getMethodName());
                ps.setString(8, iapSysApiList.get(i).getMethodDesc());
                ps.setString(9, iapSysApiList.get(i).getMethodUrl());
                ps.setString(10, iapSysApiList.get(i).getRequestType());
                ps.setShort(11, iapSysApiList.get(i).getEnabled());
                ps.setString(12, iapSysApiList.get(i).getLastUpdateBy());
                ps.setTimestamp(13, date);
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
            return iapSysApiList.size();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            ps.clearBatch();
            connection.setAutoCommit(true);
            this.closeConnection(connection, null, ps);
        }
        return 0;
    }

    /**
     * 获取当前时间的时分秒
     *
     * @return
     */
    private Timestamp getSqlDate() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 通过微服务模块名称,类路径,类的方法名和请求方式 查询
     *
     * @param iapSysApiT 需要查询的条件
     * @return
     */
    public IapSysApiT selectOne(IapSysApiT iapSysApiT) throws Exception {
        // 获取数据库连接
        Connection connection = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT A.id,A.system_code,A.model_name,A.class_name,A.class_desc,A.class_url,A.method_name,A.method_desc,A.method_url,A.request_type,A.enabled,A.last_update_by,A.last_update_date FROM iap_sys_api_t AS A WHERE A.model_name = ? AND A.class_name = ? AND A.method_name = ? AND A.request_type = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, iapSysApiT.getModelName());
            ps.setString(2, iapSysApiT.getClassName());
            ps.setString(3, iapSysApiT.getMethodName());
            ps.setString(4, iapSysApiT.getRequestType());
            rs = ps.executeQuery();
            IapSysApiT iapSysApiT1 = new IapSysApiT();
            if (rs.next()) {
                iapSysApiT1.setId(rs.getString("id"));
                iapSysApiT1.setModelName(rs.getString("model_name"));
                iapSysApiT1.setClassName(rs.getString("class_name"));
                iapSysApiT1.setMethodName(rs.getString("method_name"));
                iapSysApiT1.setRequestType(rs.getString("request_type"));
                iapSysApiT1.setEnabled(rs.getShort("enabled"));
                iapSysApiT1.setMethodDesc(rs.getString("method_desc"));
            }
            return iapSysApiT1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            this.closeConnection(connection, rs, ps);
        }
        return null;
    }


    /**
     * 通过微服务名称查询接口列表
     *
     * @param modelName 微服务名称
     * @return
     */
    public List<IapSysApiT> selectList(String modelName) throws Exception {
        // 获取数据库连接
        Connection connection = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT A.id,A.system_code,A.model_name,A.class_name,A.class_desc,A.class_url,A.method_name,A.method_desc,A.method_url,A.request_type,A.enabled,A.last_update_by,A.last_update_date FROM iap_sys_api_t AS A WHERE A.model_name = ?";
            ps = connection.prepareStatement(sql);

            ps.setString(1, modelName);
            rs = ps.executeQuery();
            List<IapSysApiT> iapSysApiList = new ArrayList<IapSysApiT>();
            while (rs.next()) {
                IapSysApiT iapSysApiT1 = new IapSysApiT();
                iapSysApiT1.setId(rs.getString("id"));
                iapSysApiT1.setSystemCode(rs.getString("system_code"));
                iapSysApiT1.setModelName(rs.getString("model_name"));
                iapSysApiT1.setClassName(rs.getString("class_name"));
                iapSysApiT1.setClassDesc(rs.getString("class_desc"));
                iapSysApiT1.setClassUrl(rs.getString("class_url"));
                iapSysApiT1.setMethodName(rs.getString("method_name"));
                iapSysApiT1.setMethodDesc(rs.getString("method_desc"));
                iapSysApiT1.setMethodUrl(rs.getString("method_url"));
                iapSysApiT1.setRequestType(rs.getString("request_type"));
                iapSysApiT1.setEnabled(rs.getShort("enabled"));
                iapSysApiT1.setLastUpdateBy(rs.getString("last_update_by"));
                iapSysApiT1.setLastUpdateDate(rs.getDate("last_update_date"));
                iapSysApiList.add(iapSysApiT1);
            }
            return iapSysApiList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            this.closeConnection(connection, rs, ps);
        }
        return null;
    }

    /**
     * 通过微服务名称，批量删除该微服务的所有接口
     *
     * @param modelName 微服务名称
     * @return Boolean
     * @throws Exception
     */
    public Boolean deleteApiModelName(String modelName) throws Exception {
        // 获取数据库连接
        Connection connection = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "DELETE FROM iap_sys_api_t WHERE iap_sys_api_t.model_name = ?";
        if (modelName != null && StringUtils.isNotEmpty(modelName)) {
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, modelName);
                ps.addBatch();
                ps.executeBatch();
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                this.closeConnection(connection, rs, ps);
            }
        }
        return false;
    }

    /**
     * 保存日志到数据库（iap_ops_log_t）表
     *
     * @param iapOpsLogDto 需要保存的日志对象
     * @throws Exception
     */
    public void insertSystemLog(IapOpsLogTDto iapOpsLogDto) throws Exception {
        // 获取数据库连接
        Connection connection = this.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO iap_ops_log_t ( id, service_id, service_name, service_ip, namespace, method_type, request_function, request_method, request_proxy, request_params, log_data, create_date, last_update_date, method_desc ) " +
                    "VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ";
            Timestamp date = this.getSqlDate();
            ps = connection.prepareStatement(sql);
            ps.setString(1, iapOpsLogDto.getId());
            ps.setString(2, iapOpsLogDto.getServiceId());
            ps.setString(3, iapOpsLogDto.getServiceName());
            ps.setString(4, iapOpsLogDto.getServiceIp());
            ps.setString(5, iapOpsLogDto.getNamespace());
            ps.setInt(6, iapOpsLogDto.getMethodType());
            ps.setString(7, iapOpsLogDto.getRequestFunction());
            ps.setString(8, iapOpsLogDto.getRequestMethod());
            ps.setString(9, iapOpsLogDto.getRequestProxy());
            ps.setString(10, iapOpsLogDto.getRequestParams());
            ps.setString(11, iapOpsLogDto.getLogData());
            ps.setTimestamp(12, date);
            ps.setTimestamp(13, date);
            ps.setString(14, iapOpsLogDto.getMethodDesc());
            ps.addBatch();
            ps.executeBatch();
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            ps.clearBatch();
            connection.setAutoCommit(true);
            this.closeConnection(connection, null, ps);
        }
    }


    /**
     * 生成 UUID
     */
    public static String uuid32() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}

package com.itl.iap.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义属性绑定值，可通过配置文件配置属性
 *
 * @author 汤俊
 * @date 2020-6-15 20:08
 * @since jdk1.8
 */
@Configuration
@ConfigurationProperties(prefix = "nacos", ignoreUnknownFields = true)
public class NacosGatewayConfig {
    //TODO 缺少高可用配置
    private String address;

    private String dataId;

    private String groupId;

    private Long timeout;

    private String nameSpace;

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

}

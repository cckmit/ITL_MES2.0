package com.itl.iap.notice.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送规则实体类
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("msg_mail_configuration")
public class MsgMailConfiguration {
    private static final long serialVersionUID = 819609707215507954L;
    public static final String PREV_CODE = "PTFS";
    public static final Integer ENABLE = 1;
    public static final Integer NO_ENABLE = 0;
    public static final Integer MAIL = 0;
    public static final Integer SMS = 1;
    /**
     * 主键
     */
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 发送类型
     */
    private Integer type;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 协议名称
     */
    private String protocol;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 启用状态
     */
    private Integer enable;

}
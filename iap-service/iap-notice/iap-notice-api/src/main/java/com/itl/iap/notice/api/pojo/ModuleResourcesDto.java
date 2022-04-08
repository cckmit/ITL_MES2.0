package com.itl.iap.notice.api.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 短信消息模板
 *
 * @author tanq
 * @date  2020/3/19
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class ModuleResourcesDto {
    //主键ID
    private String ID;

    /** 模块名称 */
    private String moduleName;
    /** 模块代码 */
    private String moduleCode;
    /** 模块路径 */
    private String modulePath;
    /** 父模块 */
    private String parentId;
    /** 模块图标 */
    private String moduleIcon;
    /** http请求方式 */
    private String httpMethod;
    /** 是否操作(0-菜单，1-http，2-路由)*/
    private Integer isOperating;
    /** 排序 */
    private Integer sort;
    /** 是否启用 */
    private Integer active;
    /** 组件地址 */
    private String component;
    /** 创建时间 */
    private Date createDate;
    /** 修改时间 */
    private Date updateDate;
    /** 最后修改人 */
    private String modifier;
    /** 备注 */
    private String remark;
    /** 创建人 */
    private String creator;

}

package com.itl.iap.system.api.entity.excelDataEntity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

/**
 * 组织架构Excel转换类
 * 修改说明：添加 @AllArgsConstructor，@NoArgsConstructor 解决启动服务报sun.misc.Unsafe.park错误
 * @author wcf
 * @date 2020-9-12
 * @version 1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysOrganizationExcelEntity implements Serializable {
    /**
     * 主键id
     */
    @Excel(name = "组织ID")
    private String id;
    /**
     * 组织编码
     */
    @Excel(name = "组织编码")
    private String code;
    /**
     * 组织名称
     */
    @Excel(name = "组织名称")
    private String name;
    /**
     * 父级组织
     */
    @Excel(name = "父级组织ID")
    private String parentOrgId;

    /**
     * 腹肌组织名称
     */
    @Excel(name = "父级名称")
    private String parentOrgName;

    /**
     * 组织地址
     */
    @Excel(name = "组织地址")
    private String address;
    /**
     * 组织类别(1公司,2部门,3销售大区,4区域)
     */
    @Excel(name="组织类别",replace = {"公司_1", "部门_2", "销售大区_3", "区域_4","无_5"},type = 10)
    private int type;

    /**
     * 组织描述
     */
    @Excel(name = "组织描述")
    private String description;
    /**
     * 是否删除(0:已删除,1:未删除)
     */
    @Excel(name="是否删除",replace = {"已删除_0", "未删除_1"},type = 10)
    //@Excel(name="是否删除",type=10)
    private int deleteStatus;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String creater;

    /**
     * 最后更新人
     */
    @Excel(name = "最后更新人")
    private String lastUpdateBy;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间",databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间",databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;

}

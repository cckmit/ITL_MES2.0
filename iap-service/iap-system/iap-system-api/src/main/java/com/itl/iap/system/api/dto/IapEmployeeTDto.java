package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 员工dto
 *
 * @author 马家伦
 * @date 2020-06-17
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapEmployeeTDto {
    // 0 在职  1 离职
    public static final Short STATUS_0 = 0;
    public static final Short STATUS_1 = 1;

    // 0 不开户  1 开户
    public static final Short OPEN_ACCOUNT_0 = 0;
    public static final Short OPEN_ACCOUNT_1 = 1;

    // 0 经销商 1 内部
    public static final Short TYPE_0 = 0;
    public static final Short TYPE_1 = 1;
    Page page;

    /**
     * 主键
     */
    @NotEmpty(message = "主键 id 不能为空", groups = {ValidationGroupUpdate.class})
    private String id;
    /**
     * 员工姓名
     */
    @NotEmpty(message = "员工姓名 name 不能为空", groups = {ValidationGroupAdd.class})
    private String name;
    /**
     * 员工编码
     */
    private String code;
    /**
     * 联系方式
     */
    private String contract;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态(0-离职，1-在职)
     */
    private Short status;
    /**
     * 类型(0-经销商，1-内部)
     */
    private Short type;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     * 创建者组织
     */
    private String createOrg;
    /**
     * 最后修改人
     */
    private String lastUpdateBy;
    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 分配的用户ID
     */
    private String userId;
    /**
     * 分配的用户名称
     */
    private String userName;
    /**
     * 类型列表
     */
    private List<Short> typeList;

    /**
     * 岗位id
     */
    private String positionId;

    /**
     * 所属组织名称
     */
    private String orgName;

    /**
     * 所属岗位名称
     */
    private String positionName;

    /**
     * 是否开户(0-否，1-是)
     */
    private Short openAccount;

}

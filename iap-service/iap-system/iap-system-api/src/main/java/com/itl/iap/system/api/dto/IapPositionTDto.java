package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 岗位dto
 *
 * @author 马家伦
 * @date 2020-06-17
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapPositionTDto {
    // 状态(0-正常状态，1-被锁定)
    public static final short ENABLED0 = 0;
    public static final short ENABLED1 = 1;

    Page page;

    /**
     * 主键
     */
    @NotEmpty(message = "主键 id 不能为空",groups = {ValidationGroupUpdate.class})
    private String id;

    /**
     * 职位名称
     */
    @NotEmpty(message = "职位名称不能为空",groups = {ValidationGroupAdd.class,ValidationGroupUpdate.class})
    private String name;

    /**
     * 职位编码
     */
    private String code;

    /**
     * 职位类型
     */
    private Short type;

    /**
     * 职位排序
     */
    private Short sort;

    /**
     *  状态(0-正常状态，1-被锁定)
     */
    private Short enabled;

    /**
     * 上级职位
     */
    private String parentId;


    /**
     * 创建组织
     */
    private String createOrg;

    /**
     * 修改人
     */
    private String lastUpdateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateDate;

    /**
     * 用户数量
     */
    private Short userNum;

    /**
     * 主要负责人姓名
     */
    private String mainLeaderName;

    /**
     * 上级职称
     */
    private String parentName;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 所属组织名称
     */
    private String orgName;

    /**
     * 子岗位列表
     */
    private List<IapPositionTDto> positionlist;

    /**
     * 岗位下用户列表
     */
    private List<IapSysUserTDto> iapSysUserTLists;



}

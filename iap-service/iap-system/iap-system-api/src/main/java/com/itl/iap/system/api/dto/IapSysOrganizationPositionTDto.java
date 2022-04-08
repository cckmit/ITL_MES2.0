package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 组织岗位中间表
 *
 * @author 马家伦
 * @date 2020-06-24
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IapSysOrganizationPositionTDto {
    /**
     * 主键
     */
    private String id;
    /**
     * 组织id
     */
    private String organizationId;
    /**
     * 岗位id
     */
    private String positionId;
    /**
     * 排序
     */
    private Short sort;
    /**
     * 状态：0:正常状态，1:被锁定
     */
    private Short state;
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
     * 创建岗位
     */
    private String createOrg;
    /**
     * 最后更新人
     */
    private String lastUpdateBy;
    /**
     * 0：正常，1：删除
     */
    private Short delFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateDate;
}

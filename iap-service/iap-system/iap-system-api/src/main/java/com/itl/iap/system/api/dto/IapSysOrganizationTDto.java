package com.itl.iap.system.api.dto;

import com.itl.iap.system.api.entity.IapPositionT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * 组织dto
 *
 * @author 李骐光
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IapSysOrganizationTDto {
    /**
     * 主键id
     */
    private String id;
    /**
     * 组织编码
     */
    private String code;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 父级组织
     */
    private String parentOrgId;
    /**
     * 组织地址
     */
    private String address;
    /**
     * 组织类别(1公司,2部门,3销售大区,4区域)
     */
    private Short type;
    /**
     * 组织描述
     */
    private String description;
    /**
     * 是否删除(0:已删除,1:未删除)
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 最后更新人
     */
    private String lastUpdateBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date lastUpdateDate;

    /**
     * 关联机构
     */
    private List<IapSysOrganizationTDto> dtoList;

    /**
     * 关联岗位表
     */
    private List<IapPositionT> positionTList;

    /**
     * 关联岗位
     */
    private List<IapPositionTDto> positionlist;
    /**
     * IM 没有分配组织的用户
     */
    private List<IapSysUserTDto> iapSysUserTDtos;
    /**
     * 父级组织名称
     */
    private String parentOrgName;
    /**
     * 下级岗位ID
     */
    private String positionId;

}

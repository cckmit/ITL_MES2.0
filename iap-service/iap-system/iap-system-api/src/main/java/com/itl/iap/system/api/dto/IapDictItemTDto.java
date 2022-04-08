package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 字典表详情表dto
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IapDictItemTDto {

    /**
     * 分页
     */
    private Page page;

    /**
     * id
     */
    private String id;

    /**
     * 字典编号
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 启动(0已启用 1 未启用)
     */
    private Short enabled;

    /**
     * 备注
     */
    private String remark;

    /**
     * 主表id
     */
    private String iapDictId;

    /**
     * 父id
     */
    private String parentId;

    private String parentCode;

    /**
     * 排序
     */
    private Short sort;


    /**
     * 字典键值
     */
    @NotEmpty(message = "字典键值不能为空", groups = {ValidationGroupAdd.class, ValidationGroupUpdate.class})
    private String keyValue;

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
     * 映射
     */
    List<IapDictItemTDto> children;

}

package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.util.group.ValidationGroupAdd;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 字典表主表dto
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IapDictTDto {

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
    @NotEmpty(message = "字典编号不能为空", groups = {ValidationGroupUpdate.class})
    private String code;
    /**
     * 字典名称
     */
    @NotEmpty(message = "字典名称不能为空", groups = {ValidationGroupAdd.class,ValidationGroupUpdate.class})
    private String name;
    /**
     * 封存(1封存 0 不封存)
     */
    private Short enabled;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Short sort;

    private String createOrg;

    private String createStation;
    /**
     * 最后修改人
     */
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;
}

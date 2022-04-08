package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EbenChan
 * @date 2020/11/21 17:50
 **/
@Data
@Accessors(chain = true)
@TableName("sim_lov")
public class SimLov extends BaseModel {
    private static final long serialVersionUID = 543915602469029960L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * lov编码
     */
    @TableField("code")
    private String code;
    /**
     * lov名称
     */
    @TableField("name")
    private String name;
    /**
     * apiUrl接口地址
     */
    @TableField("api_url")
    private String apiUrl;
}

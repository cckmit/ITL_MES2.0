package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @author liuchenghao
 * @date 2020/10/29 11:15
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_user_prove_t")
public class IapSysUserProveT extends BaseModel {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;


    /**
     * 证明Id
     */
    @TableField("PROVE_ID")
    private String proveId;


}

package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @auth liuchenghao
 * @date 2020/12/18
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_user_station_t")
public class IapSysUserStationT extends BaseModel {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;


    /**
     * 工位Id
     */
    @TableField("STATION_BO")
    private String stationBo;

    /**
     * 工厂
     */
    @TableField("SITE")
    private String site;
}

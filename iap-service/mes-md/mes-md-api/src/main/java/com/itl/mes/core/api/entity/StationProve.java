package com.itl.mes.core.api.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuchenghao
 * @date 2020/10/28 13:37
 */
@TableName("m_station_prove")
@ApiModel(value="StationProve",description="工位证明关联表")
@Data
public class StationProve extends Model<StationProve> {

    @ApiModelProperty(value="关联ID")
    @TableId(value = "ID",type= IdType.UUID)
    private String id;

    @TableField("STATION_BO")
    private String stationBo;

    @TableField("PROVE_ID")
    private String proveId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

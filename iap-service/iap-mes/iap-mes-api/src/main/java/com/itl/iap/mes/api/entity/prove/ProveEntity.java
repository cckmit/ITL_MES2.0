package com.itl.iap.mes.api.entity.prove;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/10/27 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_prove")
public class ProveEntity {


    @TableId(value = "PROVE_ID",type = IdType.UUID)
    private String proveId;

    @TableField( "SITE")
    private String site;

    @TableField( "STATE")
    private Integer state;


    @TableField( "PROVE_CODE")
    private String proveCode;


    @TableField( "PROVE_DESCRIPTION")
    private String proveDescription;

    @TableField("CREATION_DATE")
    @JsonFormat(
            pattern = CustomCommonConstants.DATE_FORMAT_CONSTANTS,
            timezone = "GMT+8"
    )
    private Date createDate;

    @TableField("LAST_UPDATE_DATE")
    @JsonFormat(
            pattern = CustomCommonConstants.DATE_FORMAT_CONSTANTS,
            timezone = "GMT+8"
    )
    private Date lastUpdateDate;

    public void setObjectSetBasicAttribute(Date date ){
        this.createDate=date;
        this.lastUpdateDate=date;
    }
}

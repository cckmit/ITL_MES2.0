package com.itl.iap.mes.api.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author liuchenghao
 * @date 2020/10/29 17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("label_type")
public class LabelTypeEntity {


    @TableId(type = IdType.UUID)
    private String id;


    @TableField( "LABEL_TYPE")
    private String labelType;



    @TableField( "LABEL_TYPE_DESCRIPTION")
    private String labelTypeDescription;


    @TableField( "SITE")
    private String site;



    @TableField( "STATE")
    private Integer state;


    @TableField( "CREATION_DATE")
    private Date creationDate;
    @TableField( "LAST_UPDATE_DATE")
    private Date lastUpdateDate;
}

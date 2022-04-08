package com.itl.iap.mes.api.entity.toolgroup;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuchenghao
 * @date 2020/11/6 14:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_tool_group_attach")
public class ToolGroupAttachEntity {



    @TableId(type = IdType.UUID)
    private String bo;


    @TableField("SITE")
    private String site;


    @TableField("TOOL_GROUP_BO")
    private String toolGroupBo;


    @TableField("CONTEXT_BO")
    private String contextBo;


    @TableField("SEQ")
    private String seq;


    @TableField("QTY")
    private Integer qty;


    @TableField("TYPE")
    private Integer type;


}

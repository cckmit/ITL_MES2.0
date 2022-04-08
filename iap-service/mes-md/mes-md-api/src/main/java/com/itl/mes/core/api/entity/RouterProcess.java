package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工艺路线路线图
 * </p>
 *
 * @author linjl
 * @since 2021-01-28
 */
@TableName("m_router_process")
@Data
@ApiModel(value="RouterProcess",description="工艺路线路线图")
public class RouterProcess extends Model<RouterProcess> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="SO:SITE,Router【PK】")
    @Length( max = 100 )
    @NotBlank
    @TableId(value = "ROUTER_BO", type = IdType.INPUT)
    private String routerBo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="流程信息")
    @Length( max = 2048 )
    @NotBlank
    @TableField("PROCESS_INFO")
    private String processInfo;

    @Override
    protected Serializable pkVal() {
        return this.routerBo;
    }

    public RouterProcess(){

    }

    public RouterProcess(String routerBo){
        this.setRouterBo(routerBo);
    }
}
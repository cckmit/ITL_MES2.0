package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工艺路线
 * </p>
 *
 * @author linjl
 * @since 2021-01-28
 */
@TableName("m_router")
@Data
@ApiModel(value="Router",description="工艺路线")
@Accessors(chain = true)
public class Router extends BaseModelHasCus<Router> {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = BOPrefixEnum.ROUTER.getPrefix();


    @ApiModelProperty(value="SO:SITE,Router【PK】")
    @Length( max = 100 )
    @NotBlank
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="工艺路线编号【UK】")
    @Length( max = 64 )
    @NotBlank
    @TableField("ROUTER")
    private String router;

    @ApiModelProperty(value="工艺路线类型")
    @Length( max = 10 )
    @TableField("ROUTER_TYPE")
    private String routerType;

    @ApiModelProperty(value="工艺路线名称")
    @Length( max = 100 )
    @TableField("ROUTER_NAME")
    private String routerName;

    @ApiModelProperty(value="工艺路线描述")
    @Length( max = 200 )
    @TableField("ROUTER_DESC")
    private String routerDesc;

    @ApiModelProperty(value="状态")
    @Length( max = 100 )
    @NotBlank
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value="版本")
    @Length( max = 3 )
    @NotBlank
    @TableField("VERSION")
    private String version;

    @ApiModelProperty(value="物料BO")
    @Length( max = 100 )
    @NotBlank
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @ApiModelProperty(value="是否为当前版本 1是 0否")
    @TableField("IS_CURRENT_VERSION")
    private int isCurrentVersion;


    @TableField(exist=false)
    @ApiModelProperty(value="流程信息")
    private RouterProcess routerProcess;

    @TableField(exist=false)
    @ApiModelProperty(value="物料")
    private Item item;



    @Override
    protected Serializable pkVal() {
        return this.bo;
    }

    public Router(){

    }

    public Router(String site, String router, String version ){

        this.bo = new StringBuilder( PREFIX ).append( ":" ).append( site ).append( "," ).append( router ).append( "," ).append( version ).toString();
        this.site = site;
        this.item = item;
        this.version = version;

    }
}
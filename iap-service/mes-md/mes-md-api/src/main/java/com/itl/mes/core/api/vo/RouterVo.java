package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.mes.core.api.entity.BaseModelHasCus;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.RouterProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工艺路线
 * </p>
 *
 * @author linjl
 * @since 2021-01-28
 */
@Data
@ApiModel(value="RouterVo",description="工艺路线")
public class RouterVo extends BaseModelHasCus<RouterVo>  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="SO:SITE,Router【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="工艺路线编号【UK】")
    private String router;

    @ApiModelProperty(value="工艺路线类型")
    private String routerType;

    @ApiModelProperty(value="工艺路线名称")
    private String routerName;

    @ApiModelProperty(value="工艺路线描述")
    private String routerDesc;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="版本")
    private String version;

    @ApiModelProperty(value="物料BO")
    private String itemBo;

    @ApiModelProperty(value="流程信息")
    private String processInfo;

    @ApiModelProperty(value = "是否是当前版本 1是 0否")
    private int isCurrentVersion;

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }
}
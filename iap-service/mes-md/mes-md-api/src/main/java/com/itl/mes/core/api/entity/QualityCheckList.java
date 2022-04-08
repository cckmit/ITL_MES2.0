package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import javax.annotation.Generated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author space
 * @since 2019-08-29
 */
@Data
@TableName("m_quality_checklist")
@ApiModel(value="QualityCheckList",description="品质检验单")
public class QualityCheckList extends Model<QualityCheckList> {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value="id")
    @TableId(value = "ID", type = IdType.AUTO)
    @Excel( name="表ID", orderNum="1" )
    private int id;

    @ApiModelProperty(value="检验单编号")
    @Length( max = 32 )
    @TableField("CHECK_CODE")
    @Excel( name="检验单编号", orderNum="2" )
    private String checkCode;

    @ApiModelProperty(value="检验工序BO")
    @Length( max = 32 )
    @TableField("OPERATION_BO")
    @Excel( name="检验工序", orderNum="3" )
    private String operationBo;

    @ApiModelProperty(value="检验产品BO")
    @Length( max = 100 )
    @TableField("ITEM_BO")
    @Excel( name="检验产品", orderNum="4" )
    private String itemBo;

    @ApiModelProperty(value="检验人")
    @Length( max = 32 )
    @TableField("CHECK_USER")
    @Excel( name="检验人", orderNum="5" )
    private String checkUser;

    @ApiModelProperty(value="检验方式")
    @Length( max = 32 )
    @TableField("CHECK_WAY")
    @Excel( name="检验方式", orderNum="6" )
    private String checkWay;

    @ApiModelProperty(value="检验类型")
    @Length( max = 32 )
    @TableField("CHECK_TYPE")
    @Excel( name="检验类型", orderNum="7" )
    private String checkType;

    @ApiModelProperty(value="检验状态")
    @Length( max = 32 )
    @TableField("CHECK_STATE")
    @Excel( name="检验状态", orderNum="8" )
    private String checkState;

    @ApiModelProperty(value="检验控制计划BO")
    @Length( max = 32 )
    @TableField("PARAMETER_BO")
    @Excel( name="检验控制计划编号", orderNum="9" )
    private String parameterBO;

    @ApiModelProperty(value="创建人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    @Excel( name="创建人", orderNum="10" )
    private String createUser;

    @ApiModelProperty(value="创建时间")
    @TableField("CREATE_DATE")
    @Excel( name="创建时间", orderNum="11",exportFormat = "yyyy-MM-dd" ,importFormat = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value="检验工序名")
    @Length( max = 32 )
    @TableField("OPERATION_NAME")
    @Excel( name="检验工序名", orderNum="12" )
    private String operationName;

    @ApiModelProperty(value="检验产品名")
    @Length( max = 32 )
    @TableField("ITEM_NAME")
    @Excel( name="检验产品名", orderNum="13" )
    private String itemName;

    @ApiModelProperty(value="创建人名")
    @Length( max = 32 )
    @TableField("CREATE_NAME")
    @Excel( name="创建人名", orderNum="14" )
    private String createName;

    @ApiModelProperty(value="检验人名")
    @Length( max = 32 )
    @TableField("CHECK_USER_NAME")
    @Excel( name="检验人名", orderNum="15" )
    private String checkUserName;

    @ApiModelProperty(value="实测值")
    @Length( max = 32 )
    @TableField("CHECK_VALUE")
    @Excel( name="实测值", orderNum="16" )
    private String checkValue;

    @ApiModelProperty(value="检验结果")
    @Length( max = 32 )
    @TableField("CHECK_RESULT")
    @Excel( name="检验结果", orderNum="17" )
    private String checkResult;

    @ApiModelProperty(value="检验描述")
    @Length( max = 32 )
    @TableField("CHECK_REMARK")
    @Excel( name="检验描述", orderNum="18" )
    private String checkRemark;

    @ApiModelProperty(value="图片地址")
    @Length( max = 255 )
    @TableField("FILE_PATH")
    @Excel( name="图片地址", orderNum="19" )
    private String filePath;

    @ApiModelProperty(value="自检人员")
    @Length( max = 32 )
    @TableField("SELF_CHECK_USER")
    @Excel( name="自检人员", orderNum="20" )
    private String selfCheckUser;

    @ApiModelProperty(value="自检时间")
    @TableField("SELF_CHECK_DATE")
    @Excel( name="自检时间", orderNum="21",exportFormat = "yyyy-MM-dd" ,importFormat = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date selfCheckDate;

    @ApiModelProperty(value="品检人员")
    @Length( max = 32 )
    @TableField("QUALITY_CHECK_USER")
    @Excel( name="品检人员", orderNum="22" )
    private String qualityCheckUser;

    @ApiModelProperty(value="品检时间")
    @TableField("QUALITY_CHECK_DATE")
    @Excel( name="品检时间", orderNum="23",exportFormat = "yyyy-MM-dd" ,importFormat = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date qualityCheckDate;

    @ApiModelProperty(value="批次")
    @Length( max = 32 )
    @TableField("SFC")
    @Excel( name="批次", orderNum="24" )
    private String sfc;


    @ApiModelProperty(value="设备名称")
    @Length( max = 100 )
    @TableField("CHECK_DEVICE_NAME")
    @Excel( name="设备名称", orderNum="25" )
    private String checkDeviceName;

    @ApiModelProperty(value="设备编号")
    @Length( max = 64 )
    @TableField("CHECK_DEVICE")
    @Excel( name="设备编号", orderNum="26" )
    private String checkDevice;

    @ApiModelProperty(value="车间BO")
    @Length( max = 64 )
    @TableField("WORKSHOP_BO")
    @Excel( name="车间BO", orderNum="27" )
    private String workshopBo;

    @ApiModelProperty(value="车间名称")
    @Length( max = 64 )
    @TableField("WORKSHOP_NAME")
    @Excel( name="车间名称", orderNum="28" )
    private String workshopName;

    @ApiModelProperty(value="物料料号")
    @Length( max = 64 )
    @TableField("ITEM")
    @Excel( name="物料料号", orderNum="29" )
    private String item;

    @ApiModelProperty(value="物料描述")
    @Length( max = 64 )
    @TableField("ITEM_DESC")
    @Excel( name="物料描述", orderNum="30" )
    private String itemDesc;

    @ApiModelProperty(value="工单编码")
    @TableField("SHOP_ORDER")
    private String shopOrder;

    @ApiModelProperty("不合格率")
    @TableField("UNQUALIFIED")
    private BigDecimal unqualified;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

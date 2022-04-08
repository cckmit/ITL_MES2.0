package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工步表
 * </p>
 *
 * @author pwy
 * @since 2021-03-11
 */
@TableName("d_dy_workstation")
@ApiModel(value="Workstation",description="工步表")
@Data
public class WorkStation extends Model<WorkStation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="OPS:SITE,OPERATION,WORKSTATION")
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="工步编码")
    @Length( max = 64 )
    @TableField("WORK_STEP_CODE")
    private String workStepCode;

    @ApiModelProperty(value="工步名称")
    @Length( max = 128 )
    @TableField("WORK_STEP_NAME")
    private String workStepName;

    @ApiModelProperty(value="工步描述")
    @Length( max = 128 )
    @TableField("WORK_STEP_DESC")
    private String workStepDesc;

    @ApiModelProperty(value="工步所属工序")
    @Length( max = 128 )
    @TableField("WORKING_PROCESS")
    private String workingProcess;

    @ApiModelProperty(value="状态")
    @Length( max = 32 )
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("UPDATED_BY")
    private String updatedBy;

    @ApiModelProperty(value="工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value="工序编码")
    @TableField(exist = false)
    private String operation;

    @ApiModelProperty(value="工艺路线编码")
    @TableField(exist = false)
    private String router;

    @ApiModelProperty(value="工艺版本")
    @TableField(exist = false)
    private String routerVersion;

    @ApiModelProperty( value = "自定义数据属性和值" )
    @TableField(exist = false)
    private List<CustomDataAndValVo> customDataAndValVoList;

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getWorkStepCode() {
        return workStepCode;
    }

    public void setWorkStepCode(String workStepCode) {
        this.workStepCode = workStepCode;
    }

    public String getWorkStepName() {
        return workStepName;
    }

    public void setWorkStepName(String workStepName) {
        this.workStepName = workStepName;
    }

    public String getWorkStepDesc() {
        return workStepDesc;
    }

    public void setWorkStepDesc(String workStepDesc) {
        this.workStepDesc = workStepDesc;
    }

    public String getWorkingProcess() {
        return workingProcess;
    }

    public void setWorkingProcess(String workingProcess) {
        this.workingProcess = workingProcess;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}

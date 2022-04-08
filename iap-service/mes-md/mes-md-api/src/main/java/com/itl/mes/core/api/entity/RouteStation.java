package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工艺路线工步配置表
 * </p>
 *
 * @author pwy
 * @since 2021-03-11
 */
@TableName("d_dy_route_station")
@ApiModel(value="Routstation",description="工步配置表")
public class RouteStation extends Model<RouteStation> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="ROUTSTATION")
    @TableId(value = "BO")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="工艺路线")
    @Length( max = 128 )
    @TableField("PROCESS_ROUTE")
    private String processRoute;

    @ApiModelProperty(value="工艺路线版本")
    @Length( max = 32 )
    @TableField("ROUTE_VER")
    private String routeVer;

    @ApiModelProperty(value="工序编码")
    @Length( max = 64 )
    @TableField("WORKING_PROCESS")
    private String workingProcess;

    @ApiModelProperty(value="工序名称")
    @Length( max = 128 )
    @TableField("WORKING_PROCESS_NAME")
    private String workingProcessName;

    @ApiModelProperty(value="工步编码")
    @Length( max = 64 )
    @TableField("WORK_STEP_CODE")
    private String workStepCode;

    @ApiModelProperty(value="工步名称")
    @Length( max = 128 )
    @TableField("WORK_STEP_NAME")
    private String workStepName;

    @ApiModelProperty(value="状态")
    @Length( max = 32 )
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value="有效性")
    @Length( max = 4 )
    @TableField("EFFECTIVE")
    private String effective;

    @ApiModelProperty(value="修改日期")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("UPDATED_BY")
    private String updatedBy;

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

    public String getProcessRoute() {
        return processRoute;
    }

    public void setProcessRoute(String processRoute) {
        this.processRoute = processRoute;
    }

    public String getRouteVer() {
        return routeVer;
    }

    public void setRouteVer(String routeVer) {
        this.routeVer = routeVer;
    }

    public String getWorkingProcess() {
        return workingProcess;
    }

    public void setWorkingProcess(String workingProcess) {
        this.workingProcess = workingProcess;
    }

    public String getWorkingProcessName() {
        return workingProcessName;
    }

    public void setWorkingProcessName(String workingProcessName) {
        this.workingProcessName = workingProcessName;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
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





    @Override
    protected Serializable pkVal() {
        return null;
    }
}

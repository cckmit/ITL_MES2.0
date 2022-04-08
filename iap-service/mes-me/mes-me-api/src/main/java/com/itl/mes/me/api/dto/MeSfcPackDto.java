package com.itl.mes.me.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/2/2
 * @since JDK1.8
 */
public class MeSfcPackDto {
    @ApiModelProperty("箱号")
    private String cartonNo;
    @ApiModelProperty("最大容量")
    private BigDecimal maxCount;
    @ApiModelProperty("当前容量")
    private BigDecimal currentCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("装箱开始时间")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("装箱结束时间")
    private Date finishTime;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("装箱明细条码")
    private List<String> sns;

    public String getCartonNo() {
        return cartonNo;
    }

    public void setCartonNo(String cartonNo) {
        this.cartonNo = cartonNo;
    }

    public BigDecimal getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(BigDecimal maxCount) {
        this.maxCount = maxCount;
    }

    public BigDecimal getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(BigDecimal currentCount) {
        this.currentCount = currentCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getSns() {
        return sns;
    }

    public void setSns(List<String> sns) {
        this.sns = sns;
    }

    public Date getcreateTime() {
        return createTime;
    }

    public void setcreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getfinishTime() {
        return finishTime;
    }

    public void setfinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public MeSfcPackDto() {
    }

    public MeSfcPackDto(String cartonNo, BigDecimal maxCount, BigDecimal currentCount, String remark, List<String> sns) {
        this.cartonNo = cartonNo;
        this.maxCount = maxCount;
        this.currentCount = currentCount;
        this.remark = remark;
        this.sns = sns;
    }
}

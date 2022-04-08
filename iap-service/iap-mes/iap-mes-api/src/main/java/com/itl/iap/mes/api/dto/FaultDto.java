package com.itl.iap.mes.api.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaultDto {
    private static final long serialVersionUID = 123250230159623466L;
    //分页对象
    private Page page;

    /**
     * 主键
     */
    private String id;
    /**
     * 工厂编码
     */
    private String planCode;
    /**
     * 故障编码
     */
    private String faultCode;
    /**
     * 描述
     */
    private String remark;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 状态
     */
    private Integer state;


    private String stateName;

    /**
     * 维修方法
     */
    private String repairMethod;

    private String siteId;

}
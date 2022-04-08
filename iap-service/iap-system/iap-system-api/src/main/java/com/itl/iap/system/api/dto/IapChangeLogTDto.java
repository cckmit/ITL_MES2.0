package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 操作变动日志(IapChangeLogT)DTO类
 *
 * @author linjs
 * @since 2020-10-30 10:40:19
 */
@Data
@Accessors(chain = true)
public class  IapChangeLogTDto implements Serializable {
    private static final long serialVersionUID = 914521949520984052L;
    //分页对象
    private Page page;

    /**
     * 主键ID
     */
          private String id;
    /**
    * 单据ID
    */    private String documentId;
    /**
    * 操作类型(1.新增；2.修改；3.删除)
    */    private Integer operatorType;
    /**
    * 操作表ID
    */    private String tableId;
    /**
    * 操作表名称
    */    private String tableName;
    /**
    * 操作人员
    */    private String creater;
    /**
    * 操作日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
    /**
    * 更新操作人员
    */    private String lastUpdateBy;
    /**
    * 更新操作日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 变动日志详情列表
     */   private List<IapChangeDetailLogTDto> iapChangeDetailLogDtoList;

}
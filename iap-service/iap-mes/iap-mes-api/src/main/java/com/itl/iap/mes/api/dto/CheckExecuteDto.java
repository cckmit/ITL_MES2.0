//package com.itl.iap.mes.api.dto;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.itl.iap.mes.api.entity.CheckExecuteItem;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class CheckExecuteDto {
//    private static final long serialVersionUID = 123250230159623466L;
//    //分页对象
//    private Page page;
//
//    @TableId(type = IdType.UUID)
//    private String id;
//
//    /**
//     * 点检计划名称
//     */
//
//    private String checkPlanName;
//    /**
//     * 设备编号
//     */
//
//    private String code;
//
//    /**
//     * 设备名称
//     */
//
//    private String name;
//
//
//    /**
//     * 设备类型
//     */
//
//    private Integer type;
//
//    /**
//     * 产线
//     */
//
//    private String productionLine;
//
//    /**
//     * 数据收集组id
//     */
//
//    private Long dataCollectionId;
//
//    /**
//     * 点检人id
//     */
//
//    private String checkUserId;
//
//    /**
//     * 点检人姓名
//     */
//
//    private String checkUserName;
//
//    /**
//     * 执行时间
//     */
//
//    private Date executeTime;
//
//
//    private List<CheckExecuteItem> checkExecuteItemList;
//
//}
package com.itl.iap.notice.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 字典详情表实体类
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IapDictItemTDto {

    /**
     * 分页
     */
    private Page page;

    /**
     * id
     */
    private String id;

    /**
     * 字典编号
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 启动(1已启用 0 未启用)
     */
    private Integer enabled;

    /**
     * 备注
     */
    private String remark;

    /**
     * 主表id
     */
    private String iapDictId;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 字典键值
     */
    private String keyValue;
    /**
     * 映射
     */
    List<IapDictItemTDto> children;
}

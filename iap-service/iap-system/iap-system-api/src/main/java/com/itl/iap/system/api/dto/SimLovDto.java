package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author EbenChan
 * @date 2020/11/21 18:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SimLovDto implements Serializable {
    private static final long serialVersionUID = -22245589193251740L;
    //分页对象
    private Page page;
    /**
     * id
     */
    private String id;
    /**
     * lov编码
     */
    private String code;
    /**
     * lov名称
     */
    private String name;
    /**
     * apiUrl接口地址
     */
    private String apiUrl;

    private List<SimLovDetailDto> simLovDetailDtoList;
}

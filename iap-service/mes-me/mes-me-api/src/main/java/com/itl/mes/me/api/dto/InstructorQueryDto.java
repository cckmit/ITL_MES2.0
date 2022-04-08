package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@Data
public class InstructorQueryDto {
    private Page page;
    private String instructor;
    private String instructorName;
    private String site;
}

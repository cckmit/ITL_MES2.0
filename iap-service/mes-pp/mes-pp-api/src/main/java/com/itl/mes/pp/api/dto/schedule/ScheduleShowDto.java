package com.itl.mes.pp.api.dto.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2021/1/26
 * @since JDK1.8
 */
@Data
public class ScheduleShowDto {
    private Page page;
    private String scheduleNo;
    private String productLine;
    private String site;
    private String item;
}

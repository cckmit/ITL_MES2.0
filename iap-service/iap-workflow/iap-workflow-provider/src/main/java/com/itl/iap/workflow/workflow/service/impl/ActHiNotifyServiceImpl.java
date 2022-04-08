package com.itl.iap.workflow.workflow.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.workflow.api.dto.ActHiNotifyDto;
import com.itl.iap.workflow.api.entity.ActHiNotify;
import org.springframework.stereotype.Service;
import com.itl.iap.workflow.workflow.mapper.ActHiNotifyMapper;
import com.itl.iap.workflow.workflow.service.IActHiNotifyService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 抄送通知(ActHiNotify)表服务实现类
 *
 * @author 罗霄
 * @date 2020-09-16
 * @since jdk1.8
 */
@Service("actHiNotifyService")
public class ActHiNotifyServiceImpl extends ServiceImpl<ActHiNotifyMapper, ActHiNotify> implements IActHiNotifyService {

    @Resource
    private ActHiNotifyMapper actHiNotifyMapper;

    /**
     * 分页查询
     *
     * @param actHiNotifyDto 抄送通知实例
     * @return IPage<ActHiNotifyDto>
     */
    @Override
    public IPage<ActHiNotifyDto> pageQuery(ActHiNotifyDto actHiNotifyDto) {
        if (null == actHiNotifyDto.getPage()) {
            actHiNotifyDto.setPage(new Page(0, 10));
        }
        return actHiNotifyMapper.pageQuery(actHiNotifyDto.getPage(), actHiNotifyDto);
    }
}
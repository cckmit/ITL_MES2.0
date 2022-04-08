package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.system.api.dto.IapOpsLogTDto;
import com.itl.iap.system.api.entity.IapOpsLogT;
import com.itl.iap.system.api.service.IapOpsLogTService;
import com.itl.iap.system.provider.mapper.IapOpsLogTMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;


/**
 * 操作日志实现类
 *
 * @author 谭强
 * @date 2020-06-29
 * @since jdk1.8
 */
@Service
public class IapOpsLogTServiceImpl extends ServiceImpl<IapOpsLogTMapper, IapOpsLogT> implements IapOpsLogTService {

    @Resource
    private IapOpsLogTMapper iapOpsLogMapper;

    /**
     * 分页查询
     *
     * @param
     * @return
     */
    @Override
    public IPage<IapOpsLogTDto> pageQuery(IapOpsLogTDto iapOpsLogDto) {
        if (null == iapOpsLogDto.getPage()) {
            iapOpsLogDto.setPage(new Page(0, 10));
        }
        return iapOpsLogMapper.pageQuery(iapOpsLogDto.getPage(), iapOpsLogDto);
    }

}

package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.system.api.dto.IapChangeDetailLogTDto;
import com.itl.iap.system.api.entity.IapChangeDetailLogT;
import com.itl.iap.system.api.service.IapChangeDetailLogTService;
import com.itl.iap.system.provider.mapper.IapChangeDetailLogTMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 操作变动明细日志(IapChangeDetailLogT)表服务实现类
 *
 * @author linjs
 * @since 2020-10-30 11:12:21
 */
@Service
@Slf4j
public class IapChangeDetailLogTServiceImpl extends ServiceImpl<IapChangeDetailLogTMapper, IapChangeDetailLogT> implements IapChangeDetailLogTService {
    
    @Resource
    private IapChangeDetailLogTMapper iapChangeDetailLogMapper;

    /**
     * 分页查询
     *
     * @param 
     * @return
     */
    @Override
    public IPage<IapChangeDetailLogTDto> pageQuery(IapChangeDetailLogTDto iapChangeDetailLogDto) {
        if (null == iapChangeDetailLogDto.getPage()) {
            iapChangeDetailLogDto.setPage(new Page(0, 10));
        }
        return iapChangeDetailLogMapper.pageQuery(iapChangeDetailLogDto.getPage(), iapChangeDetailLogDto);
    }
    
}
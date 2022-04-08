package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.system.api.dto.IapChangeLogTDto;
import com.itl.iap.system.api.entity.IapChangeDetailLogT;
import com.itl.iap.system.api.entity.IapChangeLogT;
import com.itl.iap.system.api.service.IapChangeLogTService;
import com.itl.iap.system.provider.mapper.IapChangeDetailLogTMapper;
import com.itl.iap.system.provider.mapper.IapChangeLogTMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;



/**
 * 操作变动日志(IapChangeLogT)表服务实现类
 *
 * @author linjs
 * @since 2020-10-30 10:48:36
 */
@Service
@Slf4j
public class IapChangeLogTServiceImpl extends ServiceImpl<IapChangeLogTMapper, IapChangeLogT> implements IapChangeLogTService {
    
    @Resource
    private IapChangeLogTMapper iapChangeLogMapper;

    @Resource
    private IapChangeDetailLogTMapper iapChangeDetailLogMapper;

    /**
     * 分页查询
     *
     * @param 
     * @return
     */
    @Override
    public IPage<IapChangeLogTDto> pageQuery(IapChangeLogTDto iapChangeLogDto) {
        if (null == iapChangeLogDto.getPage()) {
            iapChangeLogDto.setPage(new Page(0, 10));
        }
        return iapChangeLogMapper.pageQuery(iapChangeLogDto.getPage(), iapChangeLogDto);
    }

    /**
     * 根据操作变动ID查询操作变动明细
     * @param iapChangeLogDto 接口管理对象
     * @return
     */
    @Override
    public IapChangeLogT queryChangeDetailById(IapChangeLogTDto iapChangeLogDto) {
        String  iapChangeLogDtoId = iapChangeLogDto.getId();
        IapChangeLogT iapChangeLog = iapChangeLogMapper.selectById(iapChangeLogDtoId);
        if(null != iapChangeLog){
            iapChangeLog.setIapChangeDetailLogDtoList(iapChangeDetailLogMapper.selectList(
                    new QueryWrapper<IapChangeDetailLogT>().eq("change_log_id", iapChangeLogDtoId)));
        }

        return iapChangeLog;
    }

}

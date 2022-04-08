package com.itl.im.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.im.provider.mapper.IapImMessageCustomizeMapper;
import iap.im.api.entity.IapImMessageCustomize;
import iap.im.api.service.IapImMessageCustomizeServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * IM自定义配置Service层
 *
 * @author tanq
 * @date 2020-10-26
 * @since jdk1.8
 */
@Service
public class IapImMessageCustomizeServerImpl extends ServiceImpl<IapImMessageCustomizeMapper, IapImMessageCustomize> implements IapImMessageCustomizeServer {

    /**
     * 保存或修改配置
     *
     * @param iapImMessageCustomize
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateAll(List<IapImMessageCustomize> iapImMessageCustomize) {
        return this.saveOrUpdateBatch(iapImMessageCustomize);
    }

    /**
     * 删除配置
     *
     * @param customizeIds
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomize(List<String> customizeIds) {
        return this.removeByIds(customizeIds);

    }
}

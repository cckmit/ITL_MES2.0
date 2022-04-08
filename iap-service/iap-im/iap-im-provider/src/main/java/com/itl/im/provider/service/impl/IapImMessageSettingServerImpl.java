package com.itl.im.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctc.wstx.shaded.msv_core.reader.trex.classic.ConcurState;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.DtoUtils;
import com.itl.im.provider.mapper.IapImMessageCustomizeMapper;
import com.itl.im.provider.mapper.IapImMessageSettingMapper;
import com.itl.im.provider.util.SnowIdUtil;
import iap.im.api.dto.IapImMessageSettingDto;
import iap.im.api.entity.IapImMessageCustomize;
import iap.im.api.entity.IapImMessageSetting;
import iap.im.api.service.IapImMessageCustomizeServer;
import iap.im.api.service.IapImMessageSettingServer;
import iap.im.api.variable.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户IM配置Service层实现类
 *
 * @author tanq
 * @date 2020/10/10
 * @since jdk1.8
 */
@Service
public class IapImMessageSettingServerImpl extends ServiceImpl<IapImMessageSettingMapper, IapImMessageSetting> implements IapImMessageSettingServer {
    @Resource
    private IapImMessageSettingMapper iapImMessageSettingMapper;
    @Resource
    private IapImMessageCustomizeServer iapImMessageCustomizeServer;

    @Autowired
    private UserUtil userUtil;

    /**
     * 获取当前用户的Im配置信息
     *
     * @param iapImMessageSettingDto
     * @return
     */
    @Override
    public IapImMessageSettingDto selectByUserId(IapImMessageSettingDto iapImMessageSettingDto) {
        iapImMessageSettingDto.setUserId(userUtil.getUser().getUserName());
        return iapImMessageSettingMapper.queryUserSetting(iapImMessageSettingDto);
    }

    /**
     * 新增或修改当前用户配置
     *
     * @param messageSetting
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean intoOrUpSetting(IapImMessageSettingDto messageSetting) {
        String userName = userUtil.getUser().getUserName();
        IapImMessageSetting userSetting = iapImMessageSettingMapper.selectOne(new QueryWrapper<IapImMessageSetting>().eq("user_id", userName));
        messageSetting.setUserId(userName);
        // 新增的时候  默认为null
        if (userSetting == null || StringUtils.isEmpty(userSetting.getId())) {
            messageSetting.setId(SnowIdUtil.getLongId());
            iapImMessageSettingMapper.insert(DtoUtils.convertObj(messageSetting, IapImMessageSetting.class));
        } else {
            messageSetting.setId(userSetting.getId());
            iapImMessageSettingMapper.updateById(DtoUtils.convertObj(messageSetting, IapImMessageSetting.class));
        }
        if (!CollectionUtils.isEmpty(messageSetting.getCustomizeDto())) {
            messageSetting.getCustomizeDto().forEach(x -> {
                if (StringUtils.isEmpty(x.getId())) {
                    x.setId(SnowIdUtil.getLongId());
                }
                x.setSettingId(messageSetting.getId());
            });
            List<IapImMessageCustomize> iapImMessageCustomizes = DtoUtils.convertList(IapImMessageCustomize.class, messageSetting.getCustomizeDto());
            iapImMessageCustomizeServer.saveOrUpdateAll(iapImMessageCustomizes);
        }
        return true;
    }

    /**
     * 新增或修改当前用户配置
     *
     * @param customIds 自定义配置内容ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCustomSetting(List<String> customIds) {
        return iapImMessageCustomizeServer.deleteCustomize(customIds);
    }
}

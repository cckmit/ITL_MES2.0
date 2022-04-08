package iap.im.api.service;

import iap.im.api.dto.IapImMessageSettingDto;

import java.util.List;

/**
 * 用户IM配置Service层
 *
 * @author tanq
 * @date 2020/10/10
 * @since jdk1.8
 */
public interface IapImMessageSettingServer {

    /**
     * 获取当前用户的Im配置信息
     *
     * @param iapImMessageSettingDto
     * @return
     */
    IapImMessageSettingDto selectByUserId(IapImMessageSettingDto iapImMessageSettingDto);

    /**
     * 新增或修改配置
     *
     * @param messageSetting
     * @return
     */
    Boolean intoOrUpSetting(IapImMessageSettingDto messageSetting);
    /**
     * 新增或修改当前用户配置
     *
     * @param customIds 自定义配置内容ID
     * @return
     */
    Boolean deleteCustomSetting(List<String> customIds);
}

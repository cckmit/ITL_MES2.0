package iap.im.api.service;

import iap.im.api.entity.IapImMessageCustomize;

import java.util.List;

/**
 * 自定义消息内容回复Service层
 *
 * @author tanq
 * @date 2020/10/26
 * @since jdk1.8
 */
public interface IapImMessageCustomizeServer {

    /**
     * 保存或修改配置
     *
     * @param iapImMessageCustomize
     * @return boolean
     */
    boolean saveOrUpdateAll(List<IapImMessageCustomize> iapImMessageCustomize);

    /**
     * 删除配置
     *
     * @param customizeIds
     * @return boolean
     */
    boolean deleteCustomize(List<String> customizeIds);

}

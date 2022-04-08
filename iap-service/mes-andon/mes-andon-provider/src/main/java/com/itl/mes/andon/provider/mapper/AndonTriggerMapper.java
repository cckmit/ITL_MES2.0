package com.itl.mes.andon.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.andon.api.vo.AndonTriggerAndonVo;
import com.itl.mes.andon.api.vo.AndonTriggerPushUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
public interface AndonTriggerMapper extends BaseMapper {

    Set<AndonTriggerAndonVo> findList(@Param("idList")List<String> andonBoxBos);

    List<Map<String,String>> getStationList(String userId, String site);

    String getProductLineBo(String stationBo);

    String getWorkShopBo(String productLineBo);

    List<Map<String,String>> getDeviceList(String stationBo);


    String getItem(String itemBo);
    /**
     * 获取需要推送的用户ID
     * @param andonBo
     * @return
     */
    Set<AndonTriggerPushUserVo> getPushUser(String andonBo);


}

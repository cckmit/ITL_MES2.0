package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.Carrier;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * <p>
 * 载具表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-07-23
 */
@Repository
public interface CarrierMapper extends BaseMapper<Carrier> {

    int carrierAddUseCount( @Param( "carrierBo" ) String carrierBo,@Param( "useCount" ) int useCount, @Param( "updateDate" ) Date updateDate );
}
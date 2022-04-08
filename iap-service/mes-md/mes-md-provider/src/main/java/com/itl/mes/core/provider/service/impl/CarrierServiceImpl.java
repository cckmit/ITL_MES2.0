package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.CarrierHandleBO;
import com.itl.mes.core.api.bo.CarrierTypeHandleBO;
import com.itl.mes.core.api.entity.Carrier;
import com.itl.mes.core.api.entity.CarrierType;
import com.itl.mes.core.api.service.CarrierService;
import com.itl.mes.core.api.service.CarrierTypeService;
import com.itl.mes.core.api.vo.CarrierVo;
import com.itl.mes.core.provider.mapper.CarrierMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 载具表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-23
 */
@Service
@Transactional
public class CarrierServiceImpl extends ServiceImpl<CarrierMapper, Carrier> implements CarrierService {


    @Autowired
    private CarrierMapper carrierMapper;
    @Autowired
    private CarrierTypeService carrierTypeService;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<Carrier> selectList() {
        QueryWrapper<Carrier> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, carrier);
        return super.list(entityWrapper);
    }

    @Override
    public void save(CarrierVo carrierVo) throws CommonException {
        CarrierHandleBO carrierHandleBO = new CarrierHandleBO(UserUtils.getSite(),carrierVo.getCarrier());
        String carrierTypeBO = null;
        if(carrierVo.getCarrierType() != null&&!carrierVo.getCarrierType().equals("")){
            CarrierType exitsCarrierType = carrierTypeService.getExitsCarrierType(new CarrierTypeHandleBO(UserUtils.getSite(), carrierVo.getCarrierType()));
            carrierTypeBO = exitsCarrierType.getBo();
        }
        Carrier entityCarrier = getCarrierHandleBO(carrierHandleBO);
        Carrier carrier = new Carrier();
        BeanUtils.copyProperties(carrierVo,carrier);
        if(entityCarrier==null){
            carrier.setBo(carrierHandleBO.getBo());
            carrier.setSite(UserUtils.getSite());
            carrier.setCarrierTypeBo(carrierTypeBO);
            carrier.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),new Date());
            carrierMapper.insert(carrier);
        }else{
            CommonUtil.compareDateSame(carrierVo.getModifyDate(),entityCarrier.getModifyDate());
            carrier.setBo(carrierHandleBO.getBo());
            carrier.setCarrierTypeBo(carrierTypeBO);
            carrier.setSite(UserUtils.getSite());
            carrier.setCreateUser(entityCarrier.getCreateUser());
            carrier.setCreateDate(entityCarrier.getCreateDate());
            carrier.setModifyUser(userUtil.getUser().getUserName());
            carrier.setModifyDate(new Date());
            carrierMapper.updateById(carrier);
        }
    }

    @Override
    public CarrierVo getCarrierVoByCarrier(String carrier) throws CommonException {
        Carrier exitsCarrier = getExitsCarrierHandleBO(new CarrierHandleBO(UserUtils.getSite(), carrier));
        CarrierVo carrierVo = new CarrierVo();
        BeanUtils.copyProperties(exitsCarrier,carrierVo);
        if(exitsCarrier.getCarrierTypeBo() != null&&!exitsCarrier.getCarrierTypeBo().equals("")){
            CarrierType exitsCarrierType = carrierTypeService.getExitsCarrierType(new CarrierTypeHandleBO(exitsCarrier.getCarrierTypeBo()));
           carrierVo.setCarrierType(exitsCarrierType.getCarrierType());
        }
        return carrierVo;
    }

    @Override
    public void delete(String carrier,Date modifyDate) throws CommonException {
        Carrier exitsCarrier = getExitsCarrierHandleBO(new CarrierHandleBO(UserUtils.getSite(), carrier));
        CommonUtil.compareDateSame(modifyDate,exitsCarrier.getModifyDate());
        carrierMapper.deleteById(exitsCarrier.getBo());
    }

    @Override
    public Carrier getCarrierHandleBO(CarrierHandleBO carrierHandleBO) throws CommonException {
        Carrier carrier = carrierMapper.selectById(carrierHandleBO.getBo());
        return carrier;
    }

    @Override
    public Carrier getExitsCarrierHandleBO(CarrierHandleBO carrierHandleBO) throws CommonException {
        Carrier carrier = carrierMapper.selectById(carrierHandleBO.getBo());
        if(carrier == null)throw new CommonException("载具:"+carrierHandleBO.getCarrier()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        return carrier;
    }

    /**
     *增加载具使用次数
     *
     * @param carrierBo 载具BO
     * @param useCount 次数
     * @return int
     */
    @Override
    public int carrierAddUseCount(String carrierBo, int useCount){

        return  carrierMapper.carrierAddUseCount( carrierBo, useCount, new Date() );

    }


}
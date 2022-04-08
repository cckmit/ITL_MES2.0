package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.ShiftHandleBO;
import com.itl.mes.core.api.entity.Shift;
import com.itl.mes.core.api.entity.ShiftDetail;
import com.itl.mes.core.api.entity.WorkshopDate;
import com.itl.mes.core.api.service.ShiftDetailService;
import com.itl.mes.core.api.service.ShiftService;
import com.itl.mes.core.api.service.WorkshopDateService;
import com.itl.mes.core.api.vo.ShiftDetailVo;
import com.itl.mes.core.api.vo.ShiftVo;
import com.itl.mes.core.provider.mapper.ShiftMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 班次信息主表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@Service
@Transactional
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper, Shift> implements ShiftService {


    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private ShiftDetailService shiftDetailService;
    @Autowired
    private WorkshopDateService workshopDateService;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<Shift> selectList() {
        QueryWrapper<Shift> entityWrapper = new QueryWrapper<Shift>();
        //getEntityWrapper(entityWrapper, shift);
        return super.list(entityWrapper);
    }

    @Override
    public Shift selectShift(String shift) throws CommonException {
        QueryWrapper<Shift> entityWrapper = new QueryWrapper<Shift>();
        entityWrapper.eq(Shift.SITE, UserUtils.getSite());
        entityWrapper.eq(Shift.SHIFT,shift);
        List<Shift> shifts = shiftMapper.selectList(entityWrapper);
        if(shifts.isEmpty()){
            throw  new CommonException("班次:"+shift+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return shifts.get(0);
        }

    }

    @Override
    public ShiftVo getShiftVoByShift(String shift) throws CommonException {
        Shift shiftEntity = selectShift(shift);
        List<ShiftDetail> shiftDetailVos = shiftDetailService.getShiftDetailVos(shiftEntity.getBo());
        ShiftVo shiftVo = new ShiftVo();
        BeanUtils.copyProperties(shiftEntity,shiftVo);
       List<ShiftDetailVo> shiftDetailVoList = new ArrayList<ShiftDetailVo>();
        if(!shiftDetailVos.isEmpty()){
            for(ShiftDetail shiftDetail  :shiftDetailVos){
                ShiftDetailVo shiftDetailVo = new ShiftDetailVo();
                BeanUtils.copyProperties(shiftDetail,shiftDetailVo);
                shiftDetailVoList.add(shiftDetailVo);
            }
        }
        shiftVo.setShiftDetailVoList(shiftDetailVoList);
        return shiftVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(ShiftVo shiftVo) throws CommonException {
        ShiftHandleBO shiftHandleBO = new ShiftHandleBO(UserUtils.getSite(),shiftVo.getShift());
        Shift shiftEntity = shiftMapper.selectById(shiftHandleBO.getBo());
        if(shiftEntity==null){
              Shift shift = new Shift();
              BeanUtils.copyProperties(shiftVo,shift);
              shift.setBo(shiftHandleBO.getBo());
              shift.setSite(UserUtils.getSite());
              shift.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),new Date());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(shift);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
              shiftMapper.insert(shift);
        }else{
            CommonUtil.compareDateSame(shiftVo.getModifyDate(),shiftEntity.getModifyDate());
            List<WorkshopDate> workshopDates = workshopDateService.selectWorkshopDateList(shiftHandleBO.getBo());
            if(!workshopDates.isEmpty())throw  new CommonException("班次:"+shiftVo.getShift()+"已使用,不能修改或者删除", CommonExceptionDefinition.BASIC_EXCEPTION);
            Shift shift = new Shift();
            BeanUtils.copyProperties(shiftVo,shift);
            shift.setBo(shiftHandleBO.getBo());
            shift.setSite(UserUtils.getSite());
            shift.setCreateUser(shiftEntity.getCreateUser());
            shift.setCreateDate(shiftEntity.getCreateDate());
            shift.setModifyUser(userUtil.getUser().getUserName());
            shift.setModifyDate(new Date());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(shift);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            shiftMapper.updateById(shift);
        }
        shiftDetailService.save(shiftHandleBO.getBo(),shiftVo.getShiftDetailVoList());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String shift, Date modifyDate) throws CommonException {
        Shift shiftEntity = selectShift(shift);
        CommonUtil.compareDateSame(modifyDate,shiftEntity.getModifyDate());
        List<WorkshopDate> workshopDates = workshopDateService.selectWorkshopDateList(shiftEntity.getBo());
        if(!workshopDates.isEmpty())throw  new CommonException("班次:"+shift+"已使用,不能修改或者删除", CommonExceptionDefinition.VERIFY_EXCEPTION);
        shiftDetailService.delete(shiftEntity.getBo());
        shiftMapper.deleteById(shiftEntity.getBo());
    }


}
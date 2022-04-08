package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.mes.core.api.bo.ShiftDetailHandleBO;
import com.itl.mes.core.api.entity.ShiftDetail;
import com.itl.mes.core.api.service.ShiftDetailService;
import com.itl.mes.core.api.vo.ShiftDetailVo;
import com.itl.mes.core.provider.mapper.ShiftDetailMapper;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 班次时段明细表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@Service
@Transactional
public class ShiftDetailServiceImpl extends ServiceImpl<ShiftDetailMapper, ShiftDetail> implements ShiftDetailService {


    @Autowired
    private ShiftDetailMapper shiftDetailMapper;

    @Override
    public List<ShiftDetail> selectList() {
        QueryWrapper<ShiftDetail> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, shiftDetail);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void save(String shiftBO, List<ShiftDetailVo> shiftDetailVos) throws CommonException {
           delete(shiftBO);
           if(!shiftDetailVos.isEmpty()){
               int k = checkForDuplicates(shiftDetailVos);
               if(k != -1){
                   throw new CommonException("班次时间段重复,请检查修改后保存", CommonExceptionDefinition.VERIFY_EXCEPTION);
               }
               int i = 1;
               for(ShiftDetailVo shiftDetailVo:shiftDetailVos){
                   ShiftDetail shiftDetail = new ShiftDetail();
                   ShiftDetailHandleBO shiftDetailHandleBO = new ShiftDetailHandleBO(shiftBO,String.valueOf(i));
                   BeanUtils.copyProperties(shiftDetailVo,shiftDetail);
                   shiftDetail.setBo(shiftDetailHandleBO.getBo());
                   shiftDetail.setShiftBo(shiftBO);
                   shiftDetail.setSeq(String.valueOf(i));
                   i++;
                   shiftDetailMapper.insert(shiftDetail);
               }
           }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void delete(String shiftBO) {
           if(!StringUtils.isBlank(shiftBO)){
               QueryWrapper<ShiftDetail> entityWrapper = new QueryWrapper<>();
               entityWrapper.eq(ShiftDetail.SHIFT_BO,shiftBO);
               shiftDetailMapper.delete(entityWrapper);
           }
    }

    @Override
    public List<ShiftDetail> getShiftDetailVos(String shiftBO) throws CommonException {
        QueryWrapper<ShiftDetail> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(ShiftDetail.SHIFT_BO,shiftBO);
        List<ShiftDetail> shiftDetails = shiftDetailMapper.selectList(entityWrapper);
        return shiftDetails;
    }


    public static int checkForDuplicates(List<ShiftDetailVo> shiftDetailVos) throws CommonException {
        List<String> list = new ArrayList<String>();
        for(ShiftDetailVo shiftDetailVo:shiftDetailVos){
            if(StrUtil.isBlank(shiftDetailVo.getShiftStartDate()))throw  new CommonException("开始时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
            if(StrUtil.isBlank(shiftDetailVo.getShiftEndDate()))throw  new CommonException("结束时间不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
            list.add(shiftDetailVo.getShiftStartDate()+shiftDetailVo.getShiftEndDate()+shiftDetailVo.getIsCurrent());
        }
        int k = -1;
        for(int i = 0;i<list.size();i++){
            for(int j=(i+1);j<list.size();j++){
                if(list.get(i).equals(list.get(j))) {
                    k = j;
                    break;
                }
            }
        }
        return  k;
    }


}
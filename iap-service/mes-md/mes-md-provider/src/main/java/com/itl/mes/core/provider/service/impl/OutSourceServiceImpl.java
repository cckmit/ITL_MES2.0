package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.dto.OutSourceDto;
import com.itl.mes.core.api.entity.OutSource;
import com.itl.mes.core.api.service.OutSourceService;
import com.itl.mes.core.provider.mapper.OutSourceMapper;
import com.itl.mes.core.provider.mapper.WorkShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OutSourceServiceImpl extends ServiceImpl<OutSourceMapper, OutSource> implements OutSourceService {

    @Autowired
    private OutSourceMapper outSourceMapper;
    @Autowired
    private WorkShopMapper workShopMapper;

    @Override
    public void saveOrUpdateOutSource(OutSource outSource) {
        if(outSource.getBo()==null || outSource.getBo().equals("")){
            // 新增
            outSource.setBo(UUID.uuid32());
            outSource.setCreateTime(new Date());
            /*if(outSource.getOutsourceResult() !=1){
                outSource.setOutsourceState("委外中");
            }else {
                outSource.setOutsourceState("委外完成");
            }*/
            outSourceMapper.insert(outSource);
        }else {
            // 更新
            /*if(outSource.getOutsourceResult() !=1){
                outSource.setOutsourceState("委外中");
            }else {
                outSource.setOutsourceState("委外完成");
            }*/
            outSourceMapper.updateById(outSource);
        }
    }

    @Override
    public void deleteByBo(String bo) {
        outSourceMapper.deleteById(bo);
    }

    @Override
    public IPage<OutSource> queryPages(OutSourceDto outSourceDto) {
        return outSourceMapper.selectByCondition(outSourceDto.getPage(),outSourceDto);
    }

    @Override
    public OutSource queryByBo(String bo) {
       OutSource outSource= outSourceMapper.selectByBo(bo);
        return outSource;
    }

    @Override
    public String createCode() {
        String code=outSourceMapper.selectMaxCode();
        String resultCode=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String pfix= sdf.format(new Date());
        if(code !=null && code.contains(pfix)){
            String end=code.substring(13,16);
            int endNum= Integer.parseInt(end);
            String format = String.format("%04d", endNum+1);
            resultCode= "DYWW"+pfix+format;
        }else{
            resultCode="DYWW"+pfix+"0001";
        }

        return  resultCode;
    }

    @Override
    public List<String> selectExeceptionCode(String exceptionCode) {
        return outSourceMapper.selectExeceptionCode(exceptionCode);
    }

    @Override
    public Map<String, String> selectInfoByCode(String exceptionCode) {
        return outSourceMapper.selectInfoByCode(exceptionCode);
    }

    @Override
    public Map<String, String> selectInfoByDevice(String device) {
        return outSourceMapper.selectInfoByDevice(device);
    }


}

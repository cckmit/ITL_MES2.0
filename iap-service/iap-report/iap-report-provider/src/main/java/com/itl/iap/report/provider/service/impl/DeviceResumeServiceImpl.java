package com.itl.iap.report.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.report.api.dto.DeviceResumeDto;
import com.itl.iap.report.api.service.DeviceResumeService;
import com.itl.iap.report.api.vo.DeviceResumeVo;
import com.itl.iap.report.provider.mapper.DeviceResumeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DeviceResumeServiceImpl implements DeviceResumeService {
    @Autowired
    private DeviceResumeMapper resumeMapper;


    @Override
    public IPage<DeviceResumeVo> selectDeviceInfo(DeviceResumeDto deviceResumeDto) throws CommonException {
        IPage<DeviceResumeVo> deviceResumeVos=resumeMapper.selectDeviceInfo(deviceResumeDto.getPage(),deviceResumeDto);
        if(deviceResumeVos==null || deviceResumeVos.getRecords()==null || deviceResumeVos.getRecords().size()==0){
            throw new CommonException("不存在该检索条件下的数据信息，请重新筛选",30002);
        }

        for(DeviceResumeVo deviceResumeVo:deviceResumeVos.getRecords()){
            // 根据设备编码查询设备类型
            List<String> deviceTypes= resumeMapper.selectDeviceTypeByDevice(deviceResumeVo.getDevice());
            // 拼接设备类型
            String type="";
            for(int i=0;i<deviceTypes.size();i++){
                if(i==deviceTypes.size()-1){
                    type=type+deviceTypes.get(i);
                }else {
                    type= type+deviceTypes.get(i)+",";
                }
            }
            if(deviceResumeVo.getState() !=null && deviceResumeVo.getState().equals("1")){
                deviceResumeVo.setState("已完成");
            } else {
                deviceResumeVo.setState("未完成");
            }
            deviceResumeVo.setDeviceType(type);
        }
        return deviceResumeVos;
    }

    @Override
    public IPage<Map<String,String>>selectByCondition(DeviceResumeDto deviceResumeDto) throws CommonException {
        IPage<Map<String,String>> mapIPage=resumeMapper.selectDeviceInfoByCondition(deviceResumeDto.getPage(),deviceResumeDto);
        if(mapIPage ==null || mapIPage.getRecords()==null || mapIPage.getRecords().size() ==0){
            String workShopName=resumeMapper.selectByWorkShop(deviceResumeDto.getWorkShop());
            String deviceTypeName=resumeMapper.selectByDeviceType(deviceResumeDto.getDeviceType());
            /*String deviceName=resumeMapper.selectByDevice(deviceResumeDto.getDevice());*/
            String errMsg="不存在";
            if(deviceResumeDto.getWorkShop() !=null && deviceResumeDto.getWorkShop() !=""){
                errMsg+="车间为"+workShopName+" ";
            }
            if(deviceResumeDto.getDeviceType() !=null && deviceResumeDto.getDeviceType() !=""){
                errMsg+="设备类型为"+deviceTypeName+" ";
            }
            if(deviceResumeDto.getDevice() !=null && deviceResumeDto.getDevice() !=""){
                errMsg+="设备编码为"+deviceResumeDto.getDevice();
            }
            errMsg+="的设备信息,请重新选择筛选条件";
            throw new CommonException(errMsg,30002);
        }
        if(mapIPage !=null && mapIPage.getRecords()!=null && mapIPage.getRecords().size()>0){
            for(Map<String,String> map:mapIPage.getRecords()){
                String device=map.get("device");
                // 根据设备编码查询设备类型
                List<String> deviceTypes= resumeMapper.selectDeviceTypeByDevice(device);
                // 拼接设备类型
                String type="";
                for(int i=0;i<deviceTypes.size();i++){
                    if(i==deviceTypes.size()-1){
                        type=type+deviceTypes.get(i);
                    }else {
                        type= type+deviceTypes.get(i)+",";
                    }
                }
                map.put("deviceType",type);
            }
        }
        return mapIPage;
    };
}

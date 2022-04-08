package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.notice.client.NoticeService;
import com.itl.mes.andon.api.dto.RecordSaveDTO;
import com.itl.mes.andon.api.entity.Box;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.service.AndonTriggerService;
import com.itl.mes.andon.api.vo.AndonTriggerAndonVo;
import com.itl.mes.andon.api.vo.AndonTriggerPushUserVo;
import com.itl.mes.andon.api.vo.AndonTriggerVo;
import com.itl.mes.andon.api.vo.RecordVo;
import com.itl.mes.andon.provider.common.CommonCode;
import com.itl.mes.andon.provider.common.CommonUtils;
import com.itl.mes.andon.provider.config.Constant;
import com.itl.mes.andon.provider.exception.CustomException;
import com.itl.mes.andon.provider.mapper.AndonTriggerMapper;
import com.itl.mes.andon.provider.mapper.BoxMapper;
import com.itl.mes.andon.provider.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
@Service
public class AndonTriggerServiceImpl implements AndonTriggerService {

    @Autowired
    AndonTriggerMapper andonTriggerMapper;

    @Autowired
    BoxMapper boxMapper;

    @Autowired
    NoticeService noticeService;

    @Autowired
    RecordMapper recordMapper;

    @Value("${image.uploadpath}")
    private String uploadPath;

    @Value("${notice.itemTemplateCode}")
    private String itemTemplateCode;

    @Value("${notice.deviceTemplateCode}")
    private String deviceTemplateCode;

    @Override
    public AndonTriggerVo findAndonList() {

        AndonTriggerVo andonTriggerVo = new AndonTriggerVo();
        andonTriggerVo.setUserName(UserUtils.getCurrentUser().getUserName());
        String userId = UserUtils.getCurrentUser().getId();

        //由于一个用户可能绑定多个工位，目前功能只实现只有一个工位的情况，默认取查询到的第一个工位数据
        List<Map<String,String>> stationList = andonTriggerMapper.getStationList(userId,UserUtils.getSite());
        if(CollUtil.isEmpty(stationList)){
            throw new CustomException(CommonCode.STATION_NOT_FOUND);
        }
        String stationBo = stationList.get(0).get("stationBo");
        QueryWrapper<Box> boxQueryWrapper = new QueryWrapper<>( );
        boxQueryWrapper.eq("station_bo", stationBo);
        boxQueryWrapper.select("bo");
        Integer count = 0;
        count = boxMapper.selectCount(boxQueryWrapper);
        List<String> boxBoList = new ArrayList<>( );
        //校验工位的灯箱是否有数据，没有则查找产线，再没有则查找车间
        Set<AndonTriggerAndonVo> andonTriggerAndonVos = new HashSet<>();
        if (count > 0) {
            andonTriggerVo.setBoxName("工位灯箱");
            List<Box> boxList = boxMapper.selectList(boxQueryWrapper);
            boxList.forEach(box -> {
                boxBoList.add(box.getBo( ));
            });
            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(boxBoList));

        }
            String productLineBo = andonTriggerMapper.getProductLineBo(stationBo);
            QueryWrapper<Box> queryWrapper = new QueryWrapper<>( );
            queryWrapper.select("bo");
            queryWrapper.eq("product_line_bo", productLineBo);
            count = boxMapper.selectCount(queryWrapper);
            if (count > 0) {
                if (StrUtil.isNotBlank(andonTriggerVo.getBoxName())) {
                    andonTriggerVo.setBoxName(andonTriggerVo.getBoxName() + ",产线灯箱");
                } else {
                    andonTriggerVo.setBoxName("产线灯箱");
                }
                List<Box> boxList = boxMapper.selectList(queryWrapper);
                boxList.forEach(box -> {
                    boxBoList.add(box.getBo( ));
                });
                andonTriggerAndonVos.addAll(andonTriggerMapper.findList(boxBoList));

            }

                String workShopBo = andonTriggerMapper.getWorkShopBo(productLineBo);
                QueryWrapper<Box> wrapper = new QueryWrapper<>( );
                wrapper.select("bo");
                wrapper.eq("work_shop_bo", workShopBo);
                List<Box> boxList = boxMapper.selectList(wrapper);
                boxList.forEach(box -> {
                    boxBoList.add(box.getBo( ));
                });
                if(CollUtil.isNotEmpty(boxBoList)){
                    if (StrUtil.isNotBlank(andonTriggerVo.getBoxName())) {
                        andonTriggerVo.setBoxName(andonTriggerVo.getBoxName() + ",车间灯箱");
                    } else {
                        andonTriggerVo.setBoxName("车间灯箱");
                    }
                    andonTriggerAndonVos.addAll(andonTriggerMapper.findList(boxBoList));
                }


        List<AndonTriggerAndonVo> deleteTriggerAndonVos = new ArrayList<>();
        for(AndonTriggerAndonVo andonTriggerAndonVo : andonTriggerAndonVos){
            if(andonTriggerAndonVo.getState().equals(Constant.recordState.TRIGGER.getValue())){
                andonTriggerAndonVos.forEach(triggerAndonVo ->{
                    if(triggerAndonVo.getState().equals(Constant.recordState.REPAIT.getValue())
                            && andonTriggerAndonVo.getAndonBo().equals(triggerAndonVo.getAndonBo()) ){
                        deleteTriggerAndonVos.add(triggerAndonVo);
                    }
                });
            }else {
                continue;
            }
        }
        deleteTriggerAndonVos.forEach(andonTriggerAndonVos::remove);
        andonTriggerVo.setAndonTriggerAndonVos(andonTriggerAndonVos);
        return andonTriggerVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(RecordSaveDTO recordSaveDTO) {
        //保存日志信息前，检验一下数据是否已经存在
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("andon_bo",recordSaveDTO.getAndonBo());
        queryWrapper.eq("state",Constant.recordState.TRIGGER.getValue());
        Integer count = recordMapper.selectCount(queryWrapper);
        if(count>0){
            throw new CustomException(CommonCode.DATE_REPEAT);
        }

        Record record = new Record( );
        record.setSite(UserUtils.getSite( ));
        record.setAndonBo(recordSaveDTO.getAndonBo( ));
        record.setState(Constant.recordState.TRIGGER.getValue( ));
        if (StrUtil.isNotBlank(recordSaveDTO.getAbnormalRemark( ))) {
            record.setAbnormalRemark(recordSaveDTO.getAbnormalRemark( ));
        }
        record.setAbnormalTime(new Date( ));
        if(StrUtil.isNotBlank(recordSaveDTO.getAbnormalImg())){
            record.setAbnormalImg(recordSaveDTO.getAbnormalImg());
        }
        record.setAbnormalTime(new Date());
        record.setTriggerMan(UserUtils.getCurrentUser().getUserName());
        record.setTriggerTime(new Date());
        List<Map<String,String>> stationList = andonTriggerMapper.getStationList(UserUtils.getCurrentUser().getId(),UserUtils.getSite());
        String stationBo = stationList.get(0).get("stationBo");
        String productionBo = andonTriggerMapper.getProductLineBo(stationBo);
        String workShopBo = andonTriggerMapper.getWorkShopBo(productionBo);
        record.setProductLineBo(productionBo);
        record.setWorkShopBo(workShopBo);

        //根据什么类型的安灯，选择不同的模板，和组装参数
        String templateCode = "";
        String station = stationList.get(0).get("station");
        //paramMap 是将模板的变量替换
        Map<String,String> paramMap = new HashMap<>();
        switch (recordSaveDTO.getResourceType( )) {
            case Constant.resourceTypeDevice:

                String device = "";
                if(StrUtil.isNotBlank(record.getDeviceBo())){
                    List<Map<String,String>> deviceList = andonTriggerMapper.getDeviceList(stationBo);
                    record.setDeviceBo(deviceList.get(0).get("deviceBo"));
                    device = deviceList.get(0).get("device");
                }else {
                    record.setDeviceBo(recordSaveDTO.getDeviceBo());
                }
                record.setFaultCodeBo(recordSaveDTO.getFaultCodeBo( ));
                record.setResourceType(Constant.recordResourceType.DEVICE.getValue( ));
                if (StrUtil.isNotBlank(recordSaveDTO.getDeviceBo( ))) {
                    record.setDeviceBo(recordSaveDTO.getDeviceBo( ));
                }
                templateCode = deviceTemplateCode;
                paramMap.put("station",station);
                paramMap.put("device",device);
                break;
            case Constant.resourceTypeItem:
                templateCode = itemTemplateCode;
                String item = "";
                if (StrUtil.isNotBlank(recordSaveDTO.getItemBo( ))) {
                    record.setItemBo(recordSaveDTO.getItemBo( ));
                    item = andonTriggerMapper.getItem(recordSaveDTO.getItemBo());
                }
                paramMap.put("station",station);
                paramMap.put("item",item);
                //当前版本物料只绑定到工位的资源类型
                record.setStationBo(stationBo);
                record.setResourceType(Constant.recordResourceType.STATION.getValue( ));
                record.setCallQuantity(recordSaveDTO.getCallQuantity( ));
                break;
            default:
                break;
        }

        //组装参数发送站内信信息，如需推送其余信息，需自行拓展
        Set<AndonTriggerPushUserVo> userVos = andonTriggerMapper.getPushUser(recordSaveDTO.getAndonBo());
        for(AndonTriggerPushUserVo userVo:userVos){
            if(ObjectUtil.isNotEmpty(userVo)){
                Map<String,Object> map = new HashMap<>();
                map.put("code",templateCode);
                map.put("userId",userVo.getUserName());
                map.put("userName",userVo.getRealName());
                map.put("params",paramMap);
                noticeService.sendMessage(map);
            }
        }

        recordMapper.insert(record);
    }

    @Override
    public String upload(MultipartFile[] files) {

        String respFileName = "";
        for(int i=0; i<files.length; i++) {
            String currentDate = LocalDate.now( ).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String yyyyMMdd = uploadPath + currentDate + File.separator;
            if (FileUtil.exist(yyyyMMdd)) {
                FileUtil.mkdir(yyyyMMdd);
            }
            try {
                File file = FileUtil.writeBytes(files[i].getBytes(), yyyyMMdd + File.separator + UUID.randomUUID( ) + "@" + files[i].getOriginalFilename());
                if(StrUtil.isNotBlank(file.getName())){
                    respFileName = file.getAbsolutePath();
                }else {
                    respFileName = ","+file.getAbsolutePath();
                }
            }catch (Exception e){
                throw new CustomException(CommonCode.FILE_UPLOAD_FAIL);
            }

        }
        return respFileName;
    }

    @Override
    public RecordVo getRecord(String andonBo) {

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state","1");
        queryWrapper.eq("andon_bo",andonBo);
        Integer count = recordMapper.selectCount(queryWrapper);
        RecordVo recordVo = null;
        if(count == 1){
            recordVo = recordMapper.getRevord(andonBo);
            if(StrUtil.isNotBlank(recordVo.getAbnormalImg())){
                recordVo.setImgs(CommonUtils.convertImgToStringList(recordVo.getAbnormalImg()));
            }

        }

        return recordVo;
    }



}

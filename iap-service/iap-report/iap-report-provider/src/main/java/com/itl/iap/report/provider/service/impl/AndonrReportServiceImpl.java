package com.itl.iap.report.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.report.api.dto.*;
import com.itl.iap.report.api.entity.AndonException;
import com.itl.iap.report.api.service.AndonReportService;
import com.itl.iap.report.api.vo.AndonExceptionVo;
import com.itl.iap.report.api.vo.AndonTypeVo;
import com.itl.iap.report.api.vo.AndonVo;
import com.itl.iap.report.api.vo.AndonWarningVo;
import com.itl.iap.report.provider.mapper.AndonReportMapper;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class AndonrReportServiceImpl implements AndonReportService {
    @Autowired
    private AndonReportMapper andonReportMapper;

    @Override
    public AndonDto selectCountInfo() {
        AndonDto andonDto=new AndonDto();
        List<AndonVo> andonVos=andonReportMapper.selectCountInfo();
        andonDto.setAndonInfo(andonVos);

        // 安灯类型分布
        //查询所有安灯类型
        List<AndonVo> andonTypes=andonReportMapper.selectGroupByAndonType();
        List<AndonVo> andonTimes=andonReportMapper.selectTimeGroupByAndonType();
        List<AndonVo> andonWorkShopNames=andonReportMapper.selectGroupByWorkShopName();
        andonDto.setAndonTypes(andonTypes);
        andonDto.setAndonTimes(andonTimes);
        andonDto.setAndonWorkShopNames(andonWorkShopNames);

        // 柱状图
        // 本年所有月
        List<AndonVo> andonMonth=andonReportMapper.selectGoupByMonth();
        if(andonMonth !=null && andonMonth.size()>0){
            AndonVo andonVo=new AndonVo();
            List<String> names=new ArrayList<>();
            List<Integer> values=new ArrayList<>();
            for(AndonVo andonVom:andonMonth){
                names.add(andonVom.getName().substring(5,7));
                values.add(andonVom.getValue());
            }
            andonVo.setNames(names);
            andonVo.setValues(values);
            andonDto.setAndonSumMonth(andonVo);
        }
        // 近8周
        int week=computeWeek();
        List<String> weeks=new ArrayList<>();
        List<Integer> counts=new ArrayList<>();
        AndonVo andonWeek=new AndonVo();
        for(int i=0;i<8;i++){
            int count=andonReportMapper.selectGroupByWeek(i);
            counts.add(count);
            if(week-i<=0){
               weeks.add((week+52-i)+"w");
            }else {
               weeks.add((week-i)+"w");
            }
        }
        andonWeek.setNames(weeks);
        andonWeek.setValues(counts);
        andonDto.setAndonSumWeek(andonWeek);
        // 近七天
        List<AndonVo> andonDays=andonReportMapper.selectGroupByDay();
        if(andonDays !=null && andonDays.size()>0){
            AndonVo andonVod=new AndonVo();
            List<String> days=new ArrayList<>();
            List<Integer> dayCount=new ArrayList<>();
            int i=0;
            for(AndonVo andonVo:andonDays){
                if(i==7){
                   break;
                }
                days.add(andonVo.getName().substring(8,10));
                dayCount.add(andonVo.getValue());
                i++;
            }
            Collections.reverse(days);
            Collections.reverse(dayCount);
            andonVod.setNames(days);
            andonVod.setValues(dayCount);
            andonDto.setAndonSumDay(andonVod);
        }

        return andonDto;
    }

    @Override
    public AndonWarningDto selectAndonWarning(AndonParamDto andonParamDto) {
        AndonWarningDto andonWarningDto=new AndonWarningDto();
        Map<String, AndonWarningVo> andonWarningVoMap=new HashMap<>();
        // 待签到
        List<AndonVo> andonVoSign= andonReportMapper.selectAllInfo(0);
        if(andonVoSign !=null && andonVoSign.size()>0){
            for(AndonVo andonVo:andonVoSign){
                AndonWarningVo andonWarningVo=new AndonWarningVo();
                andonWarningVo.setWorkShopName(andonVo.getName());
                andonWarningVo.setSignValue(andonVo.getValue());
                andonWarningVoMap.put(andonVo.getName(),andonWarningVo);
            }
        }
        // 处理中
        List<AndonVo> andonHandle=andonReportMapper.selectAllInfo(1);
        if(andonHandle !=null && andonHandle.size()>0){
            for(AndonVo andonVo:andonHandle){
                // 新增
                if(andonWarningVoMap.get(andonVo.getName()) ==null){
                    AndonWarningVo andonWarningVo=new AndonWarningVo();
                    andonWarningVo.setWorkShopName(andonVo.getName());
                    andonWarningVo.setHandleValue(andonVo.getValue());
                    andonWarningVoMap.put(andonVo.getName(),andonWarningVo);
                }else {
                    AndonWarningVo andonWarningVo=andonWarningVoMap.get(andonVo.getName());
                    andonWarningVo.setHandleValue(andonVo.getValue());
                    andonWarningVoMap.put(andonVo.getName(),andonWarningVo);
                }
            }
        }
        // 已解除
        List<AndonVo> andonRelieve=andonReportMapper.selectAllInfo(2);
        if(andonRelieve !=null && andonRelieve.size()>0){
            for(AndonVo andonVo:andonRelieve){
                // 新增
                if(andonWarningVoMap.get(andonVo.getName()) ==null){
                    AndonWarningVo andonWarningVo=new AndonWarningVo();
                    andonWarningVo.setWorkShopName(andonVo.getName());
                    andonWarningVo.setRelieveValue(andonVo.getValue());
                    andonWarningVoMap.put(andonVo.getName(),andonWarningVo);
                }else {
                    AndonWarningVo andonWarningVo=andonWarningVoMap.get(andonVo.getName());
                    andonWarningVo.setRelieveValue(andonVo.getValue());
                    andonWarningVoMap.put(andonVo.getName(),andonWarningVo);
                }
            }
        }
        List<AndonWarningVo> andonWarningVos =new ArrayList<>();
        andonWarningVoMap.forEach((key,value)->{
            andonWarningVos.add(value);
        });
        andonWarningDto.setAndonWarningVos(andonWarningVos);
        IPage<AndonException> andonExceptions=andonReportMapper.selectAndonException(andonParamDto.getPage());
        andonWarningDto.setAndonExceptions(andonExceptions);
        return andonWarningDto;
    }

    @Override
    public List<AndonTypeVo> selectAndonType(AndonParamDto andonParamDto) {
        List<AndonTypeVo> andonTypeVos=andonReportMapper.selectAndonType(andonParamDto);
        if(andonTypeVos.size()>0){
            for(AndonTypeVo andonTypeVo:andonTypeVos){
                double sunTime=andonTypeVo.getSumTime();
                andonTypeVo.setAvgTime(Double.parseDouble(String.format("%.2f", sunTime/andonTypeVo.getCount())));
            }
        }
        return andonTypeVos;
    }

    @Override
    public List<AndonTypeVo> selectAndonAllTime(AndonParamDto andonParamDto) {
        List<AndonTypeVo> andonTypeVos=andonReportMapper.selectAndonAllTime(andonParamDto);
        // 合并相同安灯类型
        Map<String,AndonTypeVo> andonTypeVoMap=new HashMap<>();
        if(andonTypeVos !=null && andonTypeVos.size()>0){
            for(AndonTypeVo andonTypeVo:andonTypeVos){
                //没有新增
                if(andonTypeVoMap.get(andonTypeVo.getAndonTypeName())==null){
                    andonTypeVoMap.put(andonTypeVo.getAndonTypeName(),andonTypeVo);
                }/*else {
                AndonTypeVo andonTypeVo1=andonTypeVoMap.get(andonTypeVo.getAndonTypeName());
                andonTypeVo1.setCount(andonTypeVo1.getCount()+andonTypeVo.getCount());
                andonTypeVo1.setSumTime(andonTypeVo1.getSumTime()+andonTypeVo.getSumTime());
                andonTypeVoMap.put(andonTypeVo.getAndonTypeName(),andonTypeVo1);
            }*/
            }
        }

        List<AndonTypeVo> andonTypeVoList=new ArrayList<>();
        if(andonTypeVoMap.size()>0){
            andonTypeVoMap.forEach((key,value)->{
                andonTypeVoList.add(value);
            });
        }
        if(andonTypeVoList.size()>0){
            for(AndonTypeVo andonTypeVo:andonTypeVoList){
                double sunTime=andonTypeVo.getSumTime();
                andonTypeVo.setAvgTime(Double.parseDouble(String.format("%.2f", sunTime/andonTypeVo.getCount())));
            }
        }
        return andonTypeVoList;
    }

    @Override
    public AndonExceptionDto selectAndonExceptionInfo(AndonParamDto andonParamDto) {
        AndonExceptionDto andonExceptionDto=new AndonExceptionDto();
        IPage<AndonExceptionVo> andonExceptionList=andonReportMapper.selectAndonExceptionInfo(andonParamDto.getPage(),andonParamDto);
        if(andonExceptionList !=null && andonExceptionList.getRecords().size()>0){
            for(AndonExceptionVo andonExceptionVo:andonExceptionList.getRecords()){
                AndonStateDto andonStateDto=andonReportMapper.selectNumByWorkshopNameAndAndonTypeName(andonExceptionVo.getAndonTypeName(), andonExceptionVo.getWorkShopName(),andonParamDto);
                andonExceptionVo.setSignNum(andonStateDto.getSignNum());
                andonExceptionVo.setRunNum(andonStateDto.getRunNum());
                andonExceptionVo.setFinishedNum(andonStateDto.getFinshedNum());
                //根据车间，安灯类型，状态获取明细信息
                List<AndonException> signList=andonReportMapper.selectListByState(andonExceptionVo.getAndonTypeName(),andonExceptionVo.getWorkShopName(),"0",andonParamDto);
                List<AndonException> handleList=andonReportMapper.selectListByState(andonExceptionVo.getAndonTypeName(),andonExceptionVo.getWorkShopName(),"1",andonParamDto);
                List<AndonException> finishedList=andonReportMapper.selectListByState(andonExceptionVo.getAndonTypeName(),andonExceptionVo.getWorkShopName(),"2",andonParamDto);
                andonExceptionVo.setSignList(signList);
                andonExceptionVo.setHandleList(handleList);
                andonExceptionVo.setFinishedList(finishedList);
            }
        }

        // 获取安灯类型
        List<String> andonTypeNames=andonReportMapper.selectDistinctAndonTypeName(andonParamDto);
        List<Integer> signNum=new ArrayList<>();
        List<Integer> runNum=new ArrayList<>();
        List<Integer> finishedNum=new ArrayList<>();
        if(andonTypeNames !=null &&andonTypeNames.size()>0){
            for(String andonTypeName:andonTypeNames){
                AndonStateDto andonStateDto=andonReportMapper.selectNumByAndonTypeName(andonTypeName,andonParamDto);
                signNum.add(andonStateDto.getSignNum());
                runNum.add(andonStateDto.getRunNum());
                finishedNum.add(andonStateDto.getFinshedNum());
            }
        }
        andonExceptionDto.setAndonTypeNames(andonTypeNames);
        andonExceptionDto.setSignNums(signNum);
        andonExceptionDto.setRunNums(runNum);
        andonExceptionDto.setFinishedNums(finishedNum);
        andonExceptionDto.setAndonExceptionVoList(andonExceptionList);

        return andonExceptionDto;
    }

    // 计算当前日期是今年的第几周
    public int computeWeek(){
        Date todayDate=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(format.format(todayDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
}

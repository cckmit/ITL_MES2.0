package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.FaultDto;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.FaultMapper;
import com.itl.iap.mes.provider.mapper.LovEntryRepository;
import com.itl.iap.mes.provider.utils.ExcelReader;
import com.itl.iap.mes.provider.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;


@Service
public class FaultServiceImpl extends ServiceImpl<FaultMapper, Fault> {

    @Autowired
    private FaultMapper faultMapper;
    @Autowired
    LovEntryRepository lovEntryRepository;

    public IPage<Fault> pageQuery(FaultDto faultDto) {
        faultDto.setSiteId(UserUtils.getSite());
        if (null == faultDto.getPage()) {
            faultDto.setPage(new Page(0, 10));
        }
        return faultMapper.pageQuery(faultDto.getPage(), faultDto);
    }
    public IPage<Fault> pageQueryByState(FaultDto faultDto) {
        faultDto.setSiteId(UserUtils.getSite());
        if (null == faultDto.getPage()) {
            faultDto.setPage(new Page(0, 10));
        }
        return faultMapper.pageQueryByState(faultDto.getPage(), faultDto);
    }

    public void saveFault(Fault fault) {
        fault.setSiteId(UserUtils.getSite());
        String faultCode = fault.getFaultCode();
        if(StringUtils.isBlank(faultCode)){
            throw new CustomException(CommonCode.CODE_EMPTY);
        }

        QueryWrapper<Fault> faultQueryWrapper = new QueryWrapper<Fault>().eq("faultCode", faultCode);
        if(fault.getId() != null) faultQueryWrapper.ne("id", fault.getId());
        Fault result = faultMapper.selectOne(faultQueryWrapper);

        if(result != null){
            throw new CustomException(CommonCode.CODE_REPEAT);
        }
        if(fault.getId() != null){
            faultMapper.updateById(fault);
        }else{
            fault.setCreateTime(new Date());
            faultMapper.insert(fault);
        }
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            faultMapper.deleteById(id);
        });
    }

    public Object findById(String code) {
        Fault fault = faultMapper.selectOne(new QueryWrapper<Fault>().eq("faultCode", code));
        return fault;
    }

    @Value("${file.path}")
    private String filePath;


    public void export(HttpServletRequest request, HttpServletResponse response) {
        String[] headers = {"工厂编码","故障代码", "故障描述", "设备类型编码", "状态（0启用1关闭）", "维修方法"};
        QueryWrapper<Fault> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("siteId",UserUtils.getSite());
        List<Fault> faults = faultMapper.selectList(queryWrapper);

        List<Map<String, Object>> tableList = new ArrayList<>();
        faults.forEach(fault -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("planCode", UserUtils.getSite());
            map.put("faultCode", fault.getFaultCode());
            map.put("remark", fault.getRemark());
            map.put("type", fault.getType());
            map.put("state", Constant.EnabledEnum.getItemName(fault.getState()));
            map.put("repairMethod",fault.getRepairMethod());
            tableList.add(map);
        });
        String uuid = UUID.randomUUID().toString();
        FileUtils.createExcel(headers, tableList, filePath + uuid + ".xls");
        FileUtils.downExcel(response, filePath + uuid + ".xls");

    }

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Transactional
    public void importFile(MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> readResult = ExcelReader.readExcel2Map(file.getOriginalFilename(),inputStream);
        readResult.forEach(stringObjectMap -> {
            String faultCode = stringObjectMap.get("faultCode") == null?"":stringObjectMap.get("faultCode").toString();
            if(StringUtils.isNotBlank(faultCode)){
                Fault result = faultMapper.selectOne(new QueryWrapper<Fault>().eq("faultCode", faultCode));

                if(result != null){
                    throw new CustomException(CommonCode.CODE_REPEAT);
                }
                Fault fault = new Fault();

                fault.setFaultCode(stringObjectMap.get("faultCode")==null?"":stringObjectMap.get("faultCode").toString());
                fault.setRemark(stringObjectMap.get("remark")==null?"":stringObjectMap.get("remark").toString());
                if(stringObjectMap.get("state")!=null){
                    if(!isInteger(stringObjectMap.get("state").toString())){
                        throw new CustomException(CommonCode.IS_NOT_NUM);
                    }
                    fault.setState(Integer.valueOf(stringObjectMap.get("state").toString()));
                }
                if(stringObjectMap.get("type")!=null){
                    if(!isInteger(stringObjectMap.get("type").toString())){
                        throw new CustomException(CommonCode.IS_NOT_NUM);
                    }
                    fault.setType(stringObjectMap.get("type").toString());
                }
                fault.setRepairMethod(stringObjectMap.get("repairMethod")==null?"":stringObjectMap.get("repairMethod").toString());
                fault.setSiteId(UserUtils.getSite());
                faultMapper.insert(fault);
            }
        });
    }
}

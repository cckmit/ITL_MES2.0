package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.itl.iap.mes.api.dto.LovData;
import com.itl.iap.mes.api.dto.LovEntryData;
import com.itl.iap.mes.api.entity.Lov;
import com.itl.iap.mes.api.entity.LovEntry;
import com.itl.iap.mes.api.service.LovSearchService;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.LovEntryRepository;
import com.itl.iap.mes.provider.mapper.LovMapper;
import com.itl.iap.mes.provider.utils.SqlMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 胡广
 * @version 1.0
 * @name LovServiceImpl
 * @description
 * @date 2019-08-21
 */
@Service
public class LovServiceImpl{
    @Autowired
    private LovMapper lovMapper;
    @Autowired
    private LovEntryRepository lovEntryRepository;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private Converter<Lov, LovData> lovDataConverter;
    @Autowired
    private ApplicationContext applicationContext;



    public LovData create(LovData lovData)
    {
        return save(lovData,new Lov());
    }


    public LovData update(LovData lovData)
    {
        Lov lov = lovMapper.selectOne(new QueryWrapper<Lov>().eq("code", lovData.getCode()));

        return save(lovData,lov);
    }


    public LovData save(LovData lovData,Lov lov)
    {
        BeanUtils.copyProperties(lovData,lov);
        if(StringUtils.isNotBlank(lov.getId())){
            lovMapper.updateById(lov);
        }else{
            lovMapper.insert(lov);
        }
    //    Lov dbLov = lovRepository.save(lov);
        if(CollectionUtils.isNotEmpty(lovData.getEntries()))
        {
            for(LovEntryData lovEntryData: lovData.getEntries())
            {
                LovEntry lovEntry = new LovEntry();
                BeanUtils.copyProperties(lovEntryData,lovEntry);
                lovEntry.setLovId(lov.getId());
                if(StringUtils.isNotBlank(lovEntry.getId())){
                    lovEntryRepository.updateById(lovEntry);
                }else{
                    lovEntryRepository.insert(lovEntry);
                }

            //    lovEntryRepository.save(lovEntry);
            }
        }
        lov.setEntries(getEntries(lov.getId()));
    //    dbLov.setEntries(getEntries(dbLov.getId()));
        return lovDataConverter.convert(lov);
    }


    public Page<Lov> findAll(Lov example, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
//                .withMatcher("code", ExampleMatcher.GenericPropertyMatchers.startsWith())
//                .withIgnorePaths("sql", "params");

        return lovMapper.findAll(page,example);
    }


    public List<LovEntry> getEntries(String lovId)
    {
        return lovEntryRepository.selectList(new QueryWrapper<LovEntry>().eq("lovId",lovId));
    }


    public LovData findByCode(String code)
    {
        return lovDataConverter.convert( lovMapper.selectOne(new QueryWrapper<Lov>().eq("code",code)));
    }



    public void remove(String id)
    {
        List<LovEntry> lovEntryList = lovEntryRepository.selectList(new QueryWrapper<LovEntry>().eq("lovId", id));
        lovEntryList.forEach(lovEntry -> {
            lovEntryRepository.deleteById(lovEntry.getId());
        });
      //  lovEntryRepository.delete(lovEntryRepository.findAllByLovId(id));
        lovMapper.deleteById(id);
     //   lovRepository.delete(id);
    }


    public LovData getResultSetForLov(String lovId, Map<String, String> params) {
        Lov lov = lovMapper.selectById(lovId);
    //    Lov lov = lovMapper.findById(lovId).get();
       // Lov lov = lovRepository.findOne(lovId);
        LovData lovData = lovDataConverter.convert(lov);
        lovData.setSearchResult( getResultSet(lov.getSqlStatement(),params));
        return lovData;
    }


    public LovData showLov(String code, Map<String, String> params, Integer page, Integer pageSize) {

        Lov lov = lovMapper.selectOne(new QueryWrapper<Lov>().eq("code", code));
      //  Lov lov = lovMapper.findByCode(code);
        if(null == lov)
        {
            //throw new BaseException("找不到对应编码的Lov","INVALID_CODE");
        }
        lov.setEntries(getEntries(lov.getId()));
        if("SQL".equals(lov.getSqlTypeCode()))
        {
            return getResultSetForLov(lov,params,page,pageSize);
        }else if("SERVICE".equals(lov.getSqlTypeCode()))
        {
            LovData lovData = lovDataConverter.convert(lov);
            LovData searchResult = getServiceData(lov.getServiceExpression(),params,page,pageSize);
            lovData.setSearchResult(searchResult.getSearchResult());
            lovData.setTotal(searchResult.getTotal());
            return lovData;
        }else if("API".equals(lov.getSqlTypeCode()))
        {
            return lovDataConverter.convert(lov);
            /*
            return new RestTemplate().postForObject("http://localhost:8010"+lov.getApiUrl(),params,LovData.class,new HashMap<String,Object>(){{
                put("page",page);
                put("pageSize",pageSize);
            }});
            */
        }
        return lovDataConverter.convert(lov);
    }




    public LovData getResultSetForLov(Lov lov, Map<String, String> params, Integer page, Integer pageSize) {
        Map<String,String> searchParams = Optional.ofNullable(params).orElseGet(()->new HashMap<>());
        searchParams.put("_page", String.valueOf(page));
        searchParams.put("_pageSize", String.valueOf(pageSize));
        LovData lovData = lovDataConverter.convert(lov);
        List<Map<String,Object>> searchResult = getResultSet(lov.getSqlStatement(),searchParams);
        /*
        com.github.pagehelper.Page<Map<String,Object>> pageResult = (com.github.pagehelper.Page<Map<String, Object>>) searchResult;
        lovData.setTotal(pageResult.getTotal());
        */
        lovData.setSearchResult(searchResult);
        return lovData;
    }


    public List<Map<String, Object>> getResultSet(String statement, Map<String, String> params) {
        Optional.ofNullable(params).ifPresent(prs-> prs.forEach((k, v)->{
            if(StringUtils.isEmpty(v)) {
                params.put(k,null);
            }
        }));
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            if(null != params)
            {
                if(StringUtils.isNumeric(params.get("_page"))&&StringUtils.isNumeric(params.get("_pageSize")))
                {
                    Integer page = new Integer(params.get("_page"));
                    Integer pageSize = new Integer(params.get("_pageSize"));
                    PageHelper.startPage(page,pageSize);
                }
            }
            List<Map<String, Object>> results = new SqlMapper(sqlSession).selectList("<script>"+statement+"</script>",params);
            return results;
        }catch(Exception e){
            e.printStackTrace();
            throw new CustomException(CommonCode.FAIL);
        }
    }


    public LovData getLovData(String code)
    {
        Lov lov = lovMapper.selectOne(new QueryWrapper<Lov>().eq("code", code));
      //  Lov lov = lovMapper.findByCode(code);
        lov.setEntries(getEntries(lov.getId()));
        LovData lovData = lovDataConverter.convert(lov);
        return lovData;
    }


    public LovData getLovData(LovData lovData)
    {
        lovData.setSearchResult(getResultSet(lovData.getSqlStatement(),lovData.getSearchParams()));
        return lovData;
    }

    /**
     * 批量删除行
     * @param ids
     */

    public void removeEntries(List<Long> ids)    {
        for(Long id: ids)
        {
            if(null != id)
            {
                lovEntryRepository.deleteById(id);
              //  lovEntryRepository.delete(id);
            }
        }
    }



    public LovData getServiceData(String expression, Map<String, String> params, Integer page, Integer pageSize) {
        LovSearchService lovSearchService = applicationContext.getBean(expression, LovSearchService.class);
        return lovSearchService.search(params,page,pageSize);
    }


    @Transactional(rollbackFor = Exception.class)
    public LovData clone(String source, String target)
    {
        if(lovMapper.selectOne(new QueryWrapper<Lov>().eq("code", target))!=null)
        {
          //  throw new BaseException("被克隆对象已存在","TARGET_EXISTS");
        }
        Lov from = lovMapper.selectOne(new QueryWrapper<Lov>().eq("code", source));
        Lov to = new Lov();
        BeanUtils.copyProperties(from,to);
        to.setCode(target);
        lovMapper.insert(to);
        List<LovEntry> entries = lovEntryRepository.selectList(new QueryWrapper<LovEntry>().eq("lovId", from.getId()));
      //  List<LovEntry> entries = lovEntryRepository.findAllByLovId(from.getId());
        for(LovEntry entry: entries)
        {
            LovEntry clonedEntry = new LovEntry();
            BeanUtils.copyProperties(entry,clonedEntry);
            clonedEntry.setLovId(to.getId());
            lovEntryRepository.insert(clonedEntry);
        }
        return getLovData(target);
    }



}

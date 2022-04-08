package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.DataListHandleBO;
import com.itl.mes.core.api.bo.ListParameterHandleBO;
import com.itl.mes.core.api.entity.DataList;
import com.itl.mes.core.api.entity.DcParameter;
import com.itl.mes.core.api.entity.ListParameter;
import com.itl.mes.core.api.service.DataListService;
import com.itl.mes.core.api.service.DcParameterService;
import com.itl.mes.core.api.vo.DataListFullVo;
import com.itl.mes.core.api.vo.ListParameterVo;
import com.itl.mes.core.provider.mapper.DataListMapper;
import com.itl.mes.core.provider.mapper.ListParameterMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.itl.iap.common.base.utils.CommonUtil.checkForDuplicates;


/**
 * <p>
 * 数据列表表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-03
 */
@Service
@Transactional
public class DataListServiceImpl extends ServiceImpl<DataListMapper, DataList> implements DataListService {


    @Autowired
    private DataListMapper dataListMapper;

    @Autowired
    private ListParameterMapper listParameterMapper;

    @Autowired
    private DcParameterService dcParameterService;


    /**
     * 验证数据列表数据是否合规
     *
     * @param dataList
     * @throws CommonException
     */
    void validateDataList(DataList dataList) throws CommonException {

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(dataList);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

    }

    /**
     * 验证M_LIST_PARAMETER 参数表数据是否合规
     *
     * @param listParameter
     * @throws CommonException
     */
    void validateListParameter(ListParameter listParameter) throws CommonException {

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(listParameter);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

    }

    /**
     * 验证客户订单指定属性数据格式是否合规
     *
     * @param dataList
     * @param fields
     * @throws CommonException
     */
    void validateDataListFields(DataList dataList, String... fields) throws CommonException {
        ValidationUtil.ValidResult validResult = null;
        for (int i = 0; i < fields.length; i++) {
            validResult = ValidationUtil.validateProperty(dataList, fields[i]);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(),CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
    }


    @Override
    public List<DataList> selectList() {
        QueryWrapper<DataList> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, dataList);
        return super.list(entityWrapper);
    }

    //保存数据列表数据
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveDataList(DataListFullVo dataListFullVo) throws CommonException {

        DataListHandleBO dataListHandleBO = new DataListHandleBO(dataListFullVo.getDataList());
        DataList dataListEntity = super.getById(dataListHandleBO.getBo()); //查询数据列表

        if (dataListEntity == null) {
            //新增
            insertDataList(dataListFullVo);
        } else {
            //修改
            updateDataList(dataListEntity, dataListFullVo);
        }

    }


    private void insertDataList(DataListFullVo dataListFullVo) throws CommonException {

        DataList dataList = new DataList();
        DataListHandleBO dataListHandleBO = new DataListHandleBO(dataListFullVo.getDataList());
        dataList.setBo(dataListHandleBO.getBo());
        dataList.setDataList(dataListFullVo.getDataList());
        dataList.setListName(dataListFullVo.getListName());
        dataList.setListDesc(dataListFullVo.getListDesc());
        dataList.setObjectSetBasicAttribute(new Date());
        validateDataList(dataList);
        dataListMapper.insert(dataList);

        //明细表
        List<ListParameterVo> listParameterVoList = dataListFullVo.getListParameterVoList();
        ListParameter listParameter = new ListParameter();
        if (listParameterVoList != null && !listParameterVoList.isEmpty()) {
            List<String> list = new ArrayList<>();
            for (ListParameterVo listParameterVo : listParameterVoList) {
                list.add(listParameterVo.getFieldValue());
            }
            int i = checkForDuplicates(list);
            if (i != -1) {
                throw new CommonException("参数值"+list.get(i)+"不能重复",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            for (ListParameterVo listParameterVo : listParameterVoList) {
                if (!StrUtil.isBlank(listParameterVo.getFieldValue())) {
                    ListParameterHandleBO listParameterHandleBO = new ListParameterHandleBO(dataListHandleBO.getBo(), listParameterVo.getFieldValue());
                    listParameter.setBo(listParameterHandleBO.getBo());
                }
                listParameter.setDataListBo(dataListHandleBO.getBo());
                listParameter.setSequence(listParameterVo.getSequence());
                listParameter.setFieldValue(listParameterVo.getFieldValue());
                listParameter.setFieldName(listParameterVo.getFieldName());
                listParameter.setIsDefault(listParameterVo.getIsDefault());
                validateListParameter(listParameter);
                listParameterMapper.insert(listParameter);
            }
        }

    }


    private void updateDataList(DataList dataList, DataListFullVo dataListFullVo) throws CommonException {

        Date frontModifyDate = dataListFullVo.getModifyDate(); //前台传递的时间值
        CommonUtil.compareDateSame(frontModifyDate, dataList.getModifyDate()); //比较时间是否相等

        Date newModifyDate = new Date();
        DataList dataListEntity = new DataList();
        DataListHandleBO dataListHandleBO = new DataListHandleBO(dataListFullVo.getDataList());

        dataListEntity.setBo(dataListHandleBO.getBo());
        dataListEntity.setDataList(dataListFullVo.getDataList());
        dataListEntity.setListName(dataListFullVo.getListName());
        dataListEntity.setListDesc(dataListFullVo.getListDesc());
        dataListEntity.setModifyDate(newModifyDate);
        validateDataListFields(dataListEntity, "listName", "listDesc");
        boolean successFlag = super.updateById(dataListEntity);
        if (!successFlag) {
            throw new CommonException("数据列表" + dataListFullVo.getDataList() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        //首先删除数据库的CustomerOrderItem,在重新添加.
        QueryWrapper<ListParameter> entityWrapperLP = new QueryWrapper<>();
        entityWrapperLP.eq(ListParameter.DATA_LIST_BO, dataList.getBo());
        listParameterMapper.delete(entityWrapperLP);

        List<ListParameterVo> listParameterVoList = dataListFullVo.getListParameterVoList();
        ListParameter listParameter = new ListParameter();
        if (listParameterVoList != null && !listParameterVoList.isEmpty()) {
            List<String> list = new ArrayList<>();
            for (ListParameterVo listParameterVo : listParameterVoList) {
                list.add(listParameterVo.getFieldValue());
            }
            int i = checkForDuplicates(list);
            if (i != -1) {
                throw new CommonException("参数值"+list.get(i)+"不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            for (ListParameterVo listParameterVo : listParameterVoList) {
                ListParameterHandleBO listParameterHandleBO = new ListParameterHandleBO(dataListHandleBO.getBo(), listParameterVo.getFieldValue());
                listParameter.setBo(listParameterHandleBO.getBo());
                listParameter.setDataListBo(dataListHandleBO.getBo());
                listParameter.setSequence(listParameterVo.getSequence());
                listParameter.setFieldValue(listParameterVo.getFieldValue());
                listParameter.setFieldName(listParameterVo.getFieldName());
                listParameter.setIsDefault(listParameterVo.getIsDefault());
                listParameterMapper.insert(listParameter);
            }
        }
    }


    //删除
    @Override
    public void deleteDataListByDataList(String dataList, Date modifyDate) throws CommonException {
        DataList dataListEntity = selectByDataList(dataList);
        //判断数据列表是否 被 数据收集组使用  如使用 则不能删除
        QueryWrapper<DcParameter> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(DcParameter.SITE, UserUtils.getSite());
        entityWrapper.eq(DcParameter.DATA_LIST_BO, dataListEntity.getBo());
        List<DcParameter> dcParameters = dcParameterService.list(entityWrapper);
        if (dcParameters.size() > 0) {
            throw new CommonException("数据列表" + dataList + "被使用不能删除!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //主表根据dataList删除
        CommonUtil.compareDateSame(modifyDate, dataListEntity.getModifyDate()); //验证时间
        dataListMapper.deleteById(dataListEntity.getBo());

        //获取DataList的BO  子表根据bo删除
        DataListHandleBO dataListHandleBO = new DataListHandleBO(dataList);
        String bo = dataListHandleBO.getBo();
        QueryWrapper<ListParameter> entityWrapperLP = new QueryWrapper<>();
        entityWrapperLP.eq(ListParameter.DATA_LIST_BO, bo);
        listParameterMapper.delete(entityWrapperLP);
    }


    //根据DataList查询
    @Override
    public DataList selectByDataList(String dataList) throws CommonException {
        DataListHandleBO dataListHandleBO = new DataListHandleBO(dataList);
        DataList dataListEntity = dataListMapper.selectById(dataListHandleBO.getBo());
        if (dataListEntity == null) {
            throw new CommonException("数据列表" + dataList + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return dataListEntity;
        }

    }


    @Override
    public DataListFullVo getDataListFullVoByDataList(String dataList) throws CommonException {
        DataListHandleBO dataListHandleBO = new DataListHandleBO(dataList);
        DataList dataListEntity = dataListMapper.selectById(dataListHandleBO.getBo());
        if (dataListEntity == null) {
            throw new CommonException("数据列表" + dataList + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        DataListFullVo dataListFullVo = new DataListFullVo();
        BeanUtils.copyProperties(dataListEntity, dataListFullVo);
        dataListFullVo.setDataList(dataListEntity.getDataList());
        dataListFullVo.setListDesc(dataListEntity.getListDesc());
        dataListFullVo.setListName(dataListEntity.getListName());
        dataListFullVo.setModifyDate(dataListEntity.getModifyDate());
        QueryWrapper<ListParameter> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(ListParameter.DATA_LIST_BO, dataListHandleBO.getBo()).orderByAsc(ListParameter.SEQUENCE );
        List<ListParameter> listParameters = listParameterMapper.selectList(entityWrapper);
        List list = new ArrayList();
        for (ListParameter listParameter : listParameters) {
            ListParameterVo listParameterVo = new ListParameterVo();
            listParameterVo.setSequence(listParameter.getSequence());
            listParameterVo.setIsDefault(listParameter.getIsDefault());
            listParameterVo.setFieldValue(listParameter.getFieldValue());
            listParameterVo.setFieldName(listParameter.getFieldName());
            list.add(listParameterVo);
        }

        dataListFullVo.setListParameterVoList(list);
        return dataListFullVo;
    }
}
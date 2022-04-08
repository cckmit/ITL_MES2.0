package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.NcCodeHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.entity.NcGroup;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.NcCodeService;
import com.itl.mes.core.api.service.NcGroupMemberService;
import com.itl.mes.core.api.vo.*;
import com.itl.mes.core.provider.mapper.NcCodeMapper;
import com.itl.mes.core.provider.mapper.NcGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 不合格代码表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
@Service
@Transactional
public class NcCodeServiceImpl extends ServiceImpl<NcCodeMapper, NcCode> implements NcCodeService {


    @Autowired
    private NcCodeMapper ncCodeMapper;
    @Autowired
    private NcGroupMemberService ncGroupMemberService;
    @Autowired
    private CustomDataValService customDataValService;
    //todo 去掉工艺路线相关逻辑
    //@Autowired
    //private NcDispRouterService ncDispRouterService;
    @Autowired
    private NcGroupMapper ncGroupMapper;
    @Resource
    private UserUtil userUtil;


    @Override
    public List<NcCode> selectList() {
        QueryWrapper<NcCode> entityWrapper = new QueryWrapper<NcCode>();
        //getEntityWrapper(entityWrapper, ncCode);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void saveNcCode(NcCodeVo ncCodeVo) throws CommonException {
        String ncCodeStr = ncCodeVo.getNcCode();
        if(StrUtil.isBlank(ncCodeStr)){
            throw new  CommonException("不良代码不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        NcCodeHandleBO ncCodeBO = new NcCodeHandleBO(UserUtils.getSite(),ncCodeVo.getNcCode());

        String bo = ncCodeBO.getBo();

        NcCode ncCode1Entity = ncCodeMapper.selectById(bo);

        NcCode ncCode = new NcCode();

        if(ncCode1Entity== null){
            ncCode.setBo(bo);
            ncCode.setSite(UserUtils.getSite());
            ncCode.setNcCode(ncCodeVo.getNcCode());
            ncCode.setNcName(ncCodeVo.getNcName());
            ncCode.setNcDesc(ncCodeVo.getNcDesc());
            ncCode.setMaxNcLimit(ncCodeVo.getMaxNcLimit());
            ncCode.setPriority(ncCodeVo.getPriority());
            ncCode.setSeverity(ncCodeVo.getSeverity());
            ncCode.setNcType(ncCodeVo.getNcType());
            ncCode.setState(ncCodeVo.getState());
            ncCode.setObjectSetBasicAttribute( userUtil.getUser().getUserName(),new Date() );
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(ncCode);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            ncCodeMapper.insert(ncCode);
        }else{
            CommonUtil.compareDateSame(ncCodeVo.getModifyDate(),ncCode1Entity.getModifyDate());

            ncCode.setBo(ncCode1Entity.getBo());
            ncCode.setSite(UserUtils.getSite());
            ncCode.setNcCode(ncCodeVo.getNcCode());
            ncCode.setNcName(ncCodeVo.getNcName());
            ncCode.setNcDesc(ncCodeVo.getNcDesc());
            ncCode.setSeverity(ncCodeVo.getSeverity());
            ncCode.setNcType(ncCodeVo.getNcType());
            ncCode.setState(ncCodeVo.getState());
            ncCode.setMaxNcLimit(ncCodeVo.getMaxNcLimit());
            ncCode.setPriority(ncCodeVo.getPriority());
            ncCode.setCreateUser(ncCode1Entity.getCreateUser());
            ncCode.setCreateDate(ncCode1Entity.getCreateDate());
            Date newDate = new Date();
            ncCode.setModifyDate(newDate);
            ncCode.setModifyUser(userUtil.getUser().getUserName());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(ncCode);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            ncCodeMapper.updateById(ncCode);
            ncCodeVo.setModifyDate(newDate);
        }

            ncGroupMemberService.saveByNcCodeBO(bo, ncCodeVo.getAssignedNcGroupList());
            //todo 去掉工艺路线相关逻辑
            //ncDispRouterService.save(bo,ncCodeVo.getAssignedNcDispRouterVos());
        if(ncCodeVo.getCustomDataValVoList() !=null){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( bo );
            customDataValRequest.setSite( UserUtils.getSite() );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.NC_CODE.getDataType() );
            customDataValRequest.setCustomDataValVoList( ncCodeVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void deleteNcCode(String ncCode,Date modifyDate) throws CommonException {
        NcCodeHandleBO ncCodeHandleBO = new NcCodeHandleBO(UserUtils.getSite(),ncCode);
        NcCode ncCodeEntity = ncCodeMapper.selectById(ncCodeHandleBO.getBo());
        if(ncCodeEntity==null)throw new CommonException("不合格代码:"+ncCode+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        CommonUtil.compareDateSame(modifyDate,ncCodeEntity.getModifyDate());
        ncGroupMemberService.deleteByNcCodeBO(ncCodeEntity.getBo());
        ncCodeMapper.deleteById(ncCodeEntity.getBo());
        //todo 去掉工艺路线相关逻辑
        //ncDispRouterService.delete(ncCodeEntity.getBo());
    }

    @Override
    public NcCode selectByNcCode(String ncCode) throws CommonException {
        QueryWrapper<NcCode> entityWrapper = new QueryWrapper<>();

        entityWrapper.eq(NcCode.NC_CODE,ncCode);
        entityWrapper.eq(NcCode.SITE,UserUtils.getSite());
        List<NcCode> ncCodes = ncCodeMapper.selectList(entityWrapper);
        if(ncCodes.isEmpty()){
            throw new CommonException("不合格代码"+ncCode+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return ncCodes.get(0);
        }

    }

    @Override
    public List<NcCode> selectByNcCodeVo(NcCodeVo ncCodeVo) {
        QueryWrapper<NcCode> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(NcCode.SITE,UserUtils.getSite());
        entityWrapper.like(NcCode.NC_CODE,ncCodeVo.getNcCode());
        entityWrapper.like(NcCode.NC_NAME,ncCodeVo.getNcName());
        entityWrapper.like(NcCode.NC_DESC,ncCodeVo.getNcDesc());
        List<NcCode> ncCodes = ncCodeMapper.selectList(entityWrapper);
        return ncCodes;
    }

    @Override
    public List<NcCode> selectNcCode(String ncCode, String ncName,String ncDesc) {
        QueryWrapper<NcCode> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(NcCode.SITE,UserUtils.getSite());
        entityWrapper.like(NcCode.NC_CODE,ncCode);
        entityWrapper.like(NcCode.NC_NAME,ncName);
        entityWrapper.like(NcCode.NC_DESC,ncDesc);
        List<NcCode> ncCodes = ncCodeMapper.selectList(entityWrapper);
        return ncCodes;
    }

    @Override
    public NcCodeVo getNcCodeVoByNcCode(String ncCode) throws CommonException {
        NcCode ncCodeEntity = selectByNcCode(ncCode);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), ncCodeEntity.getBo(), CustomDataTypeEnum.NC_CODE.getDataType());
        List<Map<String, Object>> availableNcGroupList = ncCodeMapper.getAvailableNcGroupList(UserUtils.getSite(), ncCodeEntity.getBo());
        List<GroupCodeVo> available = new ArrayList<GroupCodeVo>();
        if (!availableNcGroupList.isEmpty()) {
            for (Map map : availableNcGroupList) {
                GroupCodeVo groupCodeVo = new GroupCodeVo();
                groupCodeVo.setNcGroup(map.getOrDefault("NC_GROUP","").toString());
                groupCodeVo.setNcGroupName(map.getOrDefault("NC_GROUP_NAME","").toString());
                groupCodeVo.setNcGroupDesc(map.getOrDefault("NC_GROUP","").toString());
                available.add(groupCodeVo);
            }
        }
        List<Map<String, Object>> assignedNcGroupList = ncCodeMapper.getAssignedNcGroupList(UserUtils.getSite(), ncCodeEntity.getBo());
        List<GroupCodeVo> assigned = new ArrayList<GroupCodeVo>();
        if (!assignedNcGroupList.isEmpty()) {
            for (Map map : assignedNcGroupList) {
                GroupCodeVo groupCodeVo = new GroupCodeVo();
                groupCodeVo.setNcGroup(map.getOrDefault("NC_GROUP","").toString());
                groupCodeVo.setNcGroupName(map.getOrDefault("NC_GROUP_NAME","").toString());
                groupCodeVo.setNcGroupDesc(map.getOrDefault("NC_GROUP_DESC","").toString());
                assigned.add(groupCodeVo);
            }
        }
        List<NcDispRouterVo> availableNcDispRouterVos = ncCodeMapper.getAvailableNcDispRouterVos(UserUtils.getSite(), ncCodeEntity.getBo());
        List<NcDispRouterVo> assignedNcDispRouterVos = ncCodeMapper.getAssignedNcDispRouterVos(UserUtils.getSite(), ncCodeEntity.getBo());
        NcCodeVo ncCodeVo = new NcCodeVo();
        BeanUtils.copyProperties(ncCodeEntity,ncCodeVo);
        ncCodeVo.setCustomDataAndValVoList(customDataAndValVos);
        ncCodeVo.setAvailableNcGroupList(available);
        ncCodeVo.setAssignedNcGroupList(assigned);
        ncCodeVo.setAvailableNcDispRouterVos(availableNcDispRouterVos);
        ncCodeVo.setAssignedNcDispRouterVos(assignedNcDispRouterVos);
        return ncCodeVo;
    }

    @Override
    public List<NcGroupVo> getNcGroupVoList() {
        /*RowBounds rowBounds = new RowBounds(0, CustomCommonConstants.EVERY_TIMES_SEARCH_MOST_SIZE);
        QueryWrapper<NcGroup> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(NcGroup.SITE,UserUtils.getSite());
        List<NcGroup> ncGroups = ncGroupMapper.selectPage(rowBounds, entityWrapper);*/
        List<NcGroup> ncGroups = ncGroupMapper.selectTop(UserUtils.getSite());
        List<NcGroupVo> ncGroupVos = new ArrayList<NcGroupVo>();
        if(!ncGroups.isEmpty()) {
            for(NcGroup ncGroup:ncGroups) {
                NcGroupVo ncGroupVo = new NcGroupVo();
                BeanUtils.copyProperties(ncGroup,ncGroupVo);
                ncGroupVos.add(ncGroupVo);
            }
        }
        return  ncGroupVos;
    }

}
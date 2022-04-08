package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.NcGroupHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.entity.NcGroup;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.service.NcGroupMemberService;
import com.itl.mes.core.api.service.NcGroupOperationService;
import com.itl.mes.core.api.service.NcGroupService;
import com.itl.mes.core.api.vo.*;
import com.itl.mes.core.provider.mapper.NcCodeMapper;
import com.itl.mes.core.provider.mapper.NcGroupMapper;
import com.itl.mes.core.provider.mapper.NcGroupMemberMapper;
import com.itl.mes.core.provider.mapper.OperationMapper;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
 * 不合格代码组表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
@Service
@Transactional
public class NcGroupServiceImpl extends ServiceImpl<NcGroupMapper, NcGroup> implements NcGroupService {


    @Autowired
    private NcGroupMapper ncGroupMapper;
    @Autowired
    private NcGroupOperationService ncGroupOperationService;
    @Autowired
    private OperationMapper operationMapper;
    @Autowired
    private NcGroupMemberService ncGroupMemberService;
    @Autowired
    private NcCodeMapper ncCodeMapper;
    @Autowired
    private NcGroupMemberMapper ncGroupMemberMapper;
    @Resource
    private UserUtil userUtil;


    @Override
    public List<NcGroup> selectList() {
        QueryWrapper<NcGroup> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, ncGroup);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void saveNcGroup(NcGroupVo ncGroupVo) throws CommonException {
        NcGroupHandleBO ncGroupBO = new NcGroupHandleBO(UserUtils.getSite(),ncGroupVo.getNcGroup());
        NcGroup ncGroupEntity = ncGroupMapper.selectById(ncGroupBO.getBo());
        NcGroup ncGroup = new NcGroup();
        if(ncGroupEntity==null){
            ncGroup.setBo(ncGroupBO.getBo());
            ncGroup.setSite(UserUtils.getSite());
            ncGroup.setNcGroupName(ncGroupVo.getNcGroupName());
            ncGroup.setNcGroup(ncGroupVo.getNcGroup());
            ncGroup.setNcGroupDesc(ncGroupVo.getNcGroupDesc());
            ncGroup.setIsAllResource(ncGroupVo.getIsAllResource());
            ncGroup.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),new Date());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(ncGroup);
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            ncGroupMapper.insert(ncGroup);
        }else{
            Date modifyDate = ncGroupEntity.getModifyDate();
            Date frontModifyDate = ncGroupVo.getModifyDate();
            CommonUtil.compareDateSame(frontModifyDate,modifyDate);

            ncGroup.setBo(ncGroupEntity.getBo());
            ncGroup.setSite(UserUtils.getSite());
            ncGroup.setNcGroupName(ncGroupVo.getNcGroupName());
            ncGroup.setNcGroup(ncGroupVo.getNcGroup());
            ncGroup.setNcGroupDesc(ncGroupVo.getNcGroupDesc());
            ncGroup.setIsAllResource(ncGroupVo.getIsAllResource());
            ncGroup.setCreateUser(ncGroupEntity.getCreateUser());
            ncGroup.setCreateDate(ncGroupEntity.getCreateDate());
            ncGroup.setModifyUser(userUtil.getUser().getUserName());
            ncGroup.setModifyDate(frontModifyDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(ncGroup);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(),CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            Integer updateInt = ncGroupMapper.updateById(ncGroup);
            if( updateInt==0 ){
                throw new CommonException( "数据已修改，请重新查询再执行保存操作" ,CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
            ncGroupMemberService.saveByNcGroupBO(ncGroupBO.getBo(),ncGroupVo.getAssignedNcCodeList());
            ncGroupOperationService.save(ncGroupBO.getBo(), ncGroupVo.getAssignedOperationList());

        ncGroupVo.setModifyDate(new Date());
    }

    @Override
    public NcGroup selectByNcGroup(String ncGroup) throws CommonException {
        QueryWrapper<NcGroup> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(NcGroup.SITE,UserUtils.getSite());
        entityWrapper.eq(NcGroup.NC_GROUP,ncGroup);
        List<NcGroup> ncGroups = ncGroupMapper.selectList(entityWrapper);
        if(ncGroups.isEmpty()){
         throw new CommonException("不合格代码组:"+ncGroup+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return ncGroups.get(0);
        }

    }

    @Override
    public List<NcGroup> selectByNcGroupVo(NcGroupVo ncGroupVo) {
        QueryWrapper<NcGroup> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(NcGroup.SITE,UserUtils.getSite());
        entityWrapper.like(NcGroup.NC_GROUP,ncGroupVo.getNcGroup());
        entityWrapper.like(NcGroup.NC_GROUP_NAME,ncGroupVo.getNcGroupName());
        entityWrapper.like(NcGroup.NC_GROUP_DESC,ncGroupVo.getNcGroupDesc());
        return ncGroupMapper.selectList(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void deleteByNcGroup(String ncGroup,Date modifyDat) throws CommonException {
        NcGroup ncGroupEntity = selectByNcGroup(ncGroup);
        CommonUtil.compareDateSame(modifyDat,ncGroupEntity.getModifyDate());
        ncGroupMapper.deleteById(ncGroupEntity.getBo());
        ncGroupOperationService.delete(ncGroupEntity.getBo());
    }

    @Override
    public List<NcGroup> selectNcGroup(String ncGroup, String ncGroupName ,String ncGroupDesc) {
        QueryWrapper<NcGroup> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(NcGroup.SITE,UserUtils.getSite());
        entityWrapper.like(NcGroup.NC_GROUP,ncGroup);
        entityWrapper.like(NcGroup.NC_GROUP_NAME,ncGroupName);
        entityWrapper.like(NcGroup.NC_GROUP_DESC,ncGroupDesc);
        return ncGroupMapper.selectList(entityWrapper);
    }

    @Override
    public NcGroupVo getNcGroupVoByNcGroup(String ncGroup) throws CommonException {
        NcGroup ncGroupEntity = selectByNcGroup(ncGroup);
        List<NcGroupOperationVo> availableOperationList = ncGroupMapper.getAvailableOperationList(UserUtils.getSite(), ncGroupEntity.getBo());
        List<NcGroupOperationVo> assignedOperationList = ncGroupMapper.getAssignedOperationList(UserUtils.getSite(), ncGroupEntity.getBo());
        List<CodeGroupVo> assignedNcCodeList = ncGroupMemberMapper.getAssignedNcCodeList(UserUtils.getSite(), ncGroupEntity.getBo());
        List<CodeGroupVo> availableNcCodeList = ncGroupMemberMapper.getAvailableNcCodeList(UserUtils.getSite(), ncGroupEntity.getBo());
        NcGroupVo ncGroupVo = new NcGroupVo();
        BeanUtils.copyProperties(ncGroupEntity,ncGroupVo);
        ncGroupVo.setAssignedOperationList(assignedOperationList);
        ncGroupVo.setAvailableOperationList(availableOperationList);
        ncGroupVo.setAssignedNcCodeList(assignedNcCodeList);
        ncGroupVo.setAvailabieNcCodeList(availableNcCodeList);
        return ncGroupVo;
    }

    @Override
    public List<OperationVo> getOperationList() {
        List<Operation> operations = operationMapper.selectTop(UserUtils.getSite());
        List<OperationVo> operationVos = new ArrayList<>();
        if(!operations.isEmpty()){
            for(Operation operation:operations){
                OperationVo operationVo = new OperationVo();
               BeanUtils.copyProperties(operation,operationVo);
               operationVos.add(operationVo);
            }
        }
        return operationVos;
    }

    @Override
    public List<NcCodeVo> getNcCodeVoList(String ncCode, String ncName) {
        List<NcCode> ncCodes = ncCodeMapper.selectTop(UserUtils.getSite(), ncCode, ncName);
        List<NcCodeVo> ncCodeVos = new ArrayList<>();
        if(!ncCodes.isEmpty()){
            for(NcCode ncCode1:ncCodes){
                NcCodeVo ncCodeVo = new NcCodeVo();
               BeanUtils.copyProperties(ncCode1,ncCodeVo);
                ncCodeVos.add(ncCodeVo);
            }
        }
        return ncCodeVos;
    }




}
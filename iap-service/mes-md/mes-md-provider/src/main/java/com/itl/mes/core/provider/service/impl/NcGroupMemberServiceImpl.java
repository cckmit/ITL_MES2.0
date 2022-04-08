package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.entity.NcGroup;
import com.itl.mes.core.api.entity.NcGroupMember;
import com.itl.mes.core.api.service.NcCodeService;
import com.itl.mes.core.api.service.NcGroupMemberService;
import com.itl.mes.core.api.service.NcGroupService;
import com.itl.mes.core.api.vo.CodeGroupVo;
import com.itl.mes.core.api.vo.GroupCodeVo;
import com.itl.mes.core.provider.mapper.NcGroupMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 不合格组成员表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-15
 */
@Service
@Transactional
public class NcGroupMemberServiceImpl extends ServiceImpl<NcGroupMemberMapper, NcGroupMember> implements NcGroupMemberService {


    @Autowired
    private NcGroupMemberMapper ncGroupMemberMapper;
    @Autowired
    private NcGroupService ncGroupService;
    @Autowired
    private NcCodeService ncCodeService;
    @Override
    public List<NcGroupMember> selectList() {
        QueryWrapper<NcGroupMember> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, ncGroupMember);
        return super.list(entityWrapper);
    }

    @Override
    public void deleteByNcCodeBO(String ncCodeBO) {
        QueryWrapper<NcGroupMember> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(NcGroupMember.NC_CODE_BO,ncCodeBO);
        ncGroupMemberMapper.delete(entityWrapper);
    }

    @Override
    public void saveByNcCodeBO(String ncCodeBO, List<GroupCodeVo> assignedGroupCodeVoList) throws CommonException {

            deleteByNcCodeBO(ncCodeBO);
            if(assignedGroupCodeVoList != null) {
                for (GroupCodeVo ncGroupVo : assignedGroupCodeVoList) {
                    NcGroup ncGroup = ncGroupService.selectByNcGroup(ncGroupVo.getNcGroup());
                    NcGroupMember ncGroupMember = new NcGroupMember();
                    ncGroupMember.setNcCodeBo(ncCodeBO);
                    ncGroupMember.setNcGroupBo(ncGroup.getBo());
                    ncGroupMemberMapper.insert(ncGroupMember);
                }
            }

    }

    @Override
    public void deleteByNcGroupBO(String ncGroupBO) {
        QueryWrapper<NcGroupMember> entityWrapper = new QueryWrapper<NcGroupMember>();
        entityWrapper.eq(NcGroupMember.NC_GROUP_BO,ncGroupBO);
        ncGroupMemberMapper.delete(entityWrapper);
    }

    @Override
    public void saveByNcGroupBO(String ncGroupBO, List<CodeGroupVo> assignedCodeGroupVoList) throws CommonException {
             deleteByNcGroupBO(ncGroupBO);
             if(assignedCodeGroupVoList !=null){
                 for(CodeGroupVo codeGroupVo:assignedCodeGroupVoList){
                     NcCode ncCode = ncCodeService.selectByNcCode(codeGroupVo.getNcCode());
                     NcGroupMember ncGroupMember = new NcGroupMember();
                     ncGroupMember.setNcCodeBo(ncCode.getBo());
                     ncGroupMember.setNcGroupBo(ncGroupBO);
                     ncGroupMemberMapper.insert(ncGroupMember);
                 }
             }

    }


}
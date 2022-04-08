package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcGroupMember;
import com.itl.mes.core.api.vo.CodeGroupVo;
import com.itl.mes.core.api.vo.GroupCodeVo;

import java.util.List;

/**
 * <p>
 * 不合格组成员表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-15
 */
public interface NcGroupMemberService extends IService<NcGroupMember> {

    List<NcGroupMember> selectList();

    void deleteByNcCodeBO(String ncCodeBO)throws CommonException;

    void saveByNcCodeBO(String ncCodeBO, List<GroupCodeVo> assignedGroupCodeVoList)throws CommonException;

    void deleteByNcGroupBO(String NcGroupBO)throws CommonException;

    void saveByNcGroupBO(String NcGroupBO, List<CodeGroupVo> assignedCodeGroupList)throws CommonException;

}
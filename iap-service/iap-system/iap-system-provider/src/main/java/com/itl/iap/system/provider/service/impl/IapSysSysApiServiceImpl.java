package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.system.api.dto.IapSysApiTDto;
import com.itl.iap.system.api.entity.IapSysApiT;
import com.itl.iap.system.provider.mapper.IapSysApiTMapper;
import com.itl.iap.system.api.service.IapSysApiTService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口管理实现类
 *
 * @author 马家伦
 * @date 2020-06-19
 * @since jdk1.8
 */
@Service
public class IapSysSysApiServiceImpl extends ServiceImpl<IapSysApiTMapper, IapSysApiT> implements IapSysApiTService {

    @Resource
    private IapSysApiTMapper iapSysApiMapper;


    /**
     * 通过微服务名称删除url
     *
     * @param iapSysApiDto 接口管理对象
     * @return 删除数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(IapSysApiTDto iapSysApiDto) {
        return iapSysApiMapper.delete(new QueryWrapper<IapSysApiT>().eq("model_name",iapSysApiDto.getModelName()));
        //return iapSysApiTMapper.deleteByIapSysApiT(DtoUtils.convertObj(iapSysApiTDto,IapSysApiT.class));
    }

    /**
     * 查询接口信息
     *
     * @param iapSysApiTDto 接口管理对象
     * @return 接口管理分页对象
     */
    @Override
    public IPage<IapSysApiTDto> queryList(IapSysApiTDto iapSysApiDto) {
        if (iapSysApiDto.getPage() == null) {
            iapSysApiDto.setPage(new Page(0, 10));
        }
        try {
            return iapSysApiMapper.queryList(iapSysApiDto.getPage(), DtoUtils.convertObj(iapSysApiDto,IapSysApiT.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过id查询接口信息
     *
     * @param iapSysApiDto 接口管理对象
     * @return 接口管理对象
     */
    @Override
    public IapSysApiTDto selectById(IapSysApiTDto iapSysApiDto) {
        return DtoUtils.convertObj(iapSysApiMapper.selectById(iapSysApiDto.getId()),IapSysApiTDto.class);
    }

    /**
     * 通过id列表批量删除接口
     *
     * @param iapSysApiDtoList 接口管理对象
     * @return 删除数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByIds(List<IapSysApiTDto> iapSysApiDtoList) {
        if (iapSysApiDtoList.size() > 0){
            List<String> ids = new ArrayList<String>();
            for (IapSysApiTDto iapSysApiDto : iapSysApiDtoList) {
                ids.add(iapSysApiDto .getId());
            }
            return iapSysApiMapper.deleteBatchIds(ids);
        }
        return 0;
    }
}

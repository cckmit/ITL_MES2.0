package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.system.api.dto.AuthClientDto;
import com.itl.iap.system.api.dto.IapSysResourceTDto;
import com.itl.iap.system.api.entity.IapSysResourceT;
import com.itl.iap.system.provider.mapper.IapSysResourceTMapper;
import com.itl.iap.system.api.service.IapSysResourceTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单实现类
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Service
public class IapSysResourceTServiceImpl extends ServiceImpl<IapSysResourceTMapper, IapSysResourceT> implements IapSysResourceTService {

    @Resource
    private IapSysResourceTMapper iapSysResourceMapper;
    @Autowired
    private UserUtil userUtil;

    /**
     * 查询全部菜单
     *
     * @return List<IapSysResourceTDto>
     */
    @Override
    public List<IapSysResourceTDto> queryList() {
        // 获取客户端列表
        List<AuthClientDto> allClient = iapSysResourceMapper.getAllClient(new AuthClientDto());
        // 获取菜单
        List<IapSysResourceTDto> iapSysResourceDtos = iapSysResourceMapper.queryList();
        List<IapSysResourceTDto> lists = new ArrayList();
        // 将客户端列表参数转换为菜单参数
        for (AuthClientDto str : allClient) {
            IapSysResourceTDto iapSysResourceDto = DtoUtils.convertObj(str, IapSysResourceTDto.class);
            // 菜单名称变为客户端名称
            iapSysResourceDto.setResourcesName(str.getClientName());
            // 根据客户端id 为根菜单的父id
            List<IapSysResourceTDto> collect = iapSysResourceDtos.stream().filter(x -> x.getParentId().equals(str.getId())).sorted(Comparator.comparing(IapSysResourceTDto::getSort)).collect(Collectors.toList());
            if (collect.isEmpty()) {
                lists.add(iapSysResourceDto);
//                return iapSysResourceTDtos;
            } else {
                // 递归 放子参数
                for (IapSysResourceTDto item : collect) {
                    recursiveMenu(item, iapSysResourceDtos);
                }
                // 将菜单参数和客户端名称参数进行拼接
                iapSysResourceDto.setIapSysResourceTDtos(collect);
                lists.add(iapSysResourceDto);
            }
        }
        return lists;
    }

    /**
     * 删除树
     *
     * @param iapSysResourceDto 菜单对象
     * @return Integer
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteTree(List<IapSysResourceTDto> iapSysResourceDto) {
        List<IapSysResourceTDto> dtoList = new ArrayList();
        treeToList(iapSysResourceDto, dtoList);
        List<String> collect = dtoList.stream().filter(x -> x.getId() != null).map(IapSysResourceTDto::getId).collect(Collectors.toList());
        return iapSysResourceMapper.deleteBatchIds(collect);
    }

    /**
     * 获取动态菜单
     *
     * @return List<IapSysResourceTDto>
     */
    @Override
    public List<IapSysResourceTDto> getAllMenu() {
        UserTDto user = userUtil.getUser();
        List<AuthClientDto> allClient = iapSysResourceMapper.getAllClient(new AuthClientDto().setClientId(user.getClientId()));
        List<IapSysResourceTDto> allMenu = iapSysResourceMapper.getAllMenu(user.getUserName()).stream().sorted(Comparator.comparing(IapSysResourceTDto::getSort)).collect(Collectors.toList());
        List<IapSysResourceTDto> collect = Lists.newArrayList();
        for (AuthClientDto auth : allClient) {
            List<IapSysResourceTDto> lists = allMenu.stream().filter(x -> x.getParentId().equals(auth.getId())).collect(Collectors.toList());
            for (IapSysResourceTDto iapSysResourceDto : lists) {
                collect.add(iapSysResourceDto);
            }
        }
        if (collect.isEmpty()) {
            return allMenu;
        }
        for (IapSysResourceTDto iapSys : collect) {
            recursiveMenu(iapSys, allMenu);
        }
        return collect;
    }

    /**
     * 将树转换为平面结构
     *
     * @param iapSysResourceDtoTree 要转换的树
     * @param dtoList 转换后的集合
     */
    private void treeToList(List<IapSysResourceTDto> iapSysResourceDtoTree, List<IapSysResourceTDto> dtoList) {
        if (!iapSysResourceDtoTree.isEmpty()) {
            iapSysResourceDtoTree.forEach(x -> {
                if (x.getIapSysResourceTDtos() != null && !x.getIapSysResourceTDtos().isEmpty()) {
                    treeToList(x.getIapSysResourceTDtos(), dtoList);
                }
                dtoList.add(x);
            });

        }
    }

    /**
     * 递归菜单
     *
     * @param list 父菜单
     * @param recursiveDtos 子菜单列表
     */
    private void recursiveMenu(IapSysResourceTDto list, List<IapSysResourceTDto> recursiveDtos) {
        List<IapSysResourceTDto> codeTreeList = new ArrayList<>();
        recursiveDtos.forEach(x -> {
            if (x.getParentId() != null && x.getParentId().equals(list.getId())) {
                codeTreeList.add(x);
                recursiveMenu(x, recursiveDtos);
            }
        });
        if (codeTreeList.size() > 0) {
            codeTreeList.stream().sorted(Comparator.comparing(IapSysResourceTDto::getSort)).collect(Collectors.toList());
            list.setIapSysResourceTDtos(codeTreeList);
        }
    }
}

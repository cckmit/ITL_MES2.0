package com.itl.mes.andon.api.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.andon.api.dto.TypeDTO;
import com.itl.mes.andon.api.entity.Type;
import com.itl.mes.andon.api.vo.TypeVo;

import java.util.List;
import java.util.Map;

/**
 * 安灯类型
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
public interface TypeService extends IService<Type> {

    /**
     * 保存安灯类型数据
     * @param typeVo
     * @throws CommonException
     */
    void saveByTypeVo(TypeVo typeVo) throws CommonException;

    /**
     * 根据type安灯类型编号获取TypeVo
     * @param andonType
     * @return
     * @throws CommonException
     */
    TypeVo getTypeVoByType(String andonType) throws CommonException;

    /**
     * 根据type安灯类型编号获取Type
     * @param type
     * @return
     * @throws CommonException
     */
    Type selectByType(String type) throws CommonException;

    /**
     * 通过对象模糊查询
     * @param typeVo
     * @return
     * @throws CommonException
     */
    List<Type> selectByTypeVo(TypeVo typeVo) throws CommonException;

    /**
     * 多条件模糊查询
     * @param type
     * @param typeName
     * @param typeDesc
     * @return
     * @throws CommonException
     */
    List<Type> selectType(String type, String typeName, String typeDesc) throws CommonException;

    /**
     * 删除安灯类型数据
     * @param types
     * @return
     * @throws CommonException
     */
    int delete(List<String> bos) throws CommonException;

    /**
     * 分页查询安灯类型
     * @param typeDTO
     * @return
     * @throws CommonException
     */
    IPage<Type> queryPage(TypeDTO typeDTO) throws CommonException;

    /**
     * 分页查询可用的安灯类型
     * @param typeDTO
     * @return
     * @throws CommonException
     */
    IPage<Type> queryPageForUse(TypeDTO typeDTO) throws CommonException;
}


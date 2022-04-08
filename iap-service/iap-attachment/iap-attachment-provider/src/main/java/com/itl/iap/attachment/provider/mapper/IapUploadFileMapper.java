package com.itl.iap.attachment.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.attachment.api.dto.IapUploadFileDto;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件上传mapper
 *
 * @author 谭强
 * @date 2020-08-20
 * @since jdk1.8
 */
public interface IapUploadFileMapper extends BaseMapper<IapUploadFile> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param iapUploadFileTDto 文件上传实例
     * @return IPage<IapUploadFileDto>
     */
    IPage<IapUploadFileDto> pageQuery(Page page, @Param("iapUploadFileTDto") IapUploadFileDto iapUploadFileTDto);

    /**
     * 批量修改状态
     * @param onType 状态
     * @param idList id列表
     * @return int
     */
    int updateByIds(@Param("onType") Short onType, @Param("idList") List<String> idList);

}
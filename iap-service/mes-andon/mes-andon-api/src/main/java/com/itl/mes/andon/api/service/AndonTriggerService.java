package com.itl.mes.andon.api.service;

import com.itl.mes.andon.api.dto.RecordSaveDTO;
import com.itl.mes.andon.api.vo.AndonTriggerVo;
import com.itl.mes.andon.api.vo.RecordVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
public interface AndonTriggerService {

    /**
     * 获取操作者绑定的安灯信息
     * @return
     */
    AndonTriggerVo findAndonList();

    /**
     *保存异常日志信息
     * @param recordSaveDTO
     */
    void saveRecord(RecordSaveDTO recordSaveDTO);

    /**
     * 上传文件
     * @param files
     * @return
     */
    String upload(MultipartFile[] files);

    /**
     * 获取异常日志信息
     * @return
     */
    RecordVo getRecord(String andonBo);
}

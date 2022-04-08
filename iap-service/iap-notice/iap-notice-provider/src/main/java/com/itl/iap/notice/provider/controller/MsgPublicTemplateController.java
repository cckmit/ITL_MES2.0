package com.itl.iap.notice.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.notice.api.dto.IapDictItemTDto;
import com.itl.iap.notice.api.dto.MsgPublicTemplateDto;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.pojo.ResponseCode;
import com.itl.iap.notice.api.service.MsgPublicTemplateService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公共消息模板(包含邮件、系统消息、短信模板等，如果是短信模板类型，则需要关联短信消息模板表)(MsgPublicTemplate)表控制层
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
@RestController
@RequestMapping("/msgPublicTemplate")
public class MsgPublicTemplateController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private MsgPublicTemplateService msgPublicTemplateService;

    @PostMapping("/add")
    @ApiOperation(value = "新增消息模板", notes = "新增消息模板")
    public ResponseData<MsgPublicTemplate> addRecord(@RequestBody MsgPublicTemplate record) {
        msgPublicTemplateService.add(record);
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改消息模板", notes = "修改消息模板")
    public ResponseData updateRecord(@RequestBody MsgPublicTemplate record) {

        return ResponseData.success(msgPublicTemplateService.update(record));
    }

    @PostMapping("/table")
    @ApiOperation(value = "分页查询消息模板", notes = "分页查询消息模板")
    public ResponseData<IPage<MsgPublicTemplateDto>> query(@RequestBody MsgPublicTemplateDto query) {
        return ResponseData.success(msgPublicTemplateService.query(query));
    }

    /**
     * 根据id获取消息模板详情
     *
     * @return
     */
    @GetMapping("/getMsgPublicTemplateInf/id/{id}")
    @ApiOperation(value = "根据id获取消息模板详情", notes = "根据id获取消息模板详情")
    public ResponseData<MsgPublicTemplateDto> getMsgPublicTemplateInfById(@PathVariable("id") final String id) {
        return ResponseData.success(msgPublicTemplateService.getById(id));
    }

    /**
     * 获取code消息模板详情
     *
     * @return
     */
    @GetMapping("/getMsgPublicTemplateInf/code/{code}")
    @ApiOperation(value = "获取code消息模板详情", notes = "获取code消息模板详情")
    public ResponseData<MsgPublicTemplate> getMsgPublicTemplateInfByCode(@PathVariable("code") final String code) {
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), msgPublicTemplateService.getMsgPublicTemplateByCode(code));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除消息模板", notes = "删除消息模板")
    public ResponseData<MsgPublicTemplate> deleteRecord(@RequestBody List<MsgPublicTemplate> record) {

        logger.info("删除消息模板");
        try {
            if (!(CollectionUtils.isEmpty(record))) {
                record.stream().forEach(item -> {
                    msgPublicTemplateService.removeById(item.getId());
                });
            } else {
                logger.error("删除数据为空");
                return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
            }
        } catch (Exception e) {
            logger.error("删除消息模板失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @GetMapping("getCode/{code}")
    public ResponseData<List<IapDictItemTDto>> getCode(@PathVariable("code") String code) {
        return new ResponseData<List<IapDictItemTDto>>(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage(),
                msgPublicTemplateService.getCode(code));
    }

}
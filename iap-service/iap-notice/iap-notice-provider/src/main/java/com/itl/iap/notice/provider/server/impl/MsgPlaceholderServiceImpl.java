package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.notice.api.dto.MsgPlaceholderDto;
import com.itl.iap.notice.api.entity.MsgPlaceholder;
import com.itl.iap.notice.api.entity.MsgPlaceholderType;
import com.itl.iap.notice.api.service.MsgPlaceholderService;
import com.itl.iap.notice.provider.mapper.MsgPlaceholderMapper;
import com.itl.iap.notice.provider.mapper.MsgPlaceholderTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * (MsgPlaceholder)表实现类
 *
 * @author liaochengdian
 * @date 2020-04-07
 * @since jdk1.8
 */
@Service("msgPlaceholderService")
public class MsgPlaceholderServiceImpl extends ServiceImpl<MsgPlaceholderMapper, MsgPlaceholder> implements MsgPlaceholderService {

    private Logger logger = LoggerFactory.getLogger(MsgPlaceholderService.class);
    @Resource
    private MsgPlaceholderMapper msgPlaceholderMapper;
    @Resource
    private MsgPlaceholderTypeMapper msgPlaceholderTypeMapper;

    @Override
    public boolean checkOwn(MsgPlaceholder record) {
        String typeName = record.getTypeName();
        MsgPlaceholderType msgPlaceholderType = msgPlaceholderTypeMapper.selectByName(typeName);
        QueryWrapper<MsgPlaceholder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", record.getName());
        //此查询条件是验证修改操作时，占位符名称验证
        if (!StringUtils.isEmpty(record.getId())) {
            queryWrapper.eq("id", record.getId());
        }
        //不同分组下可以有相同的占位符名称
        if (msgPlaceholderType == null) {
            return false;
        } else {
            queryWrapper.eq("msgPlaceholderTypeId", msgPlaceholderType.getId());
        }
        List<MsgPlaceholder> msgPlaceholderList = msgPlaceholderMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(msgPlaceholderList)) {
            return true;
        }
        return false;
    }

    /**
     * 根据id获取占位符详情
     *
     * @param id
     * @return MsgPlaceholder
     */
    @Override
    public MsgPlaceholder selectById(String id) {
        return msgPlaceholderMapper.selectMsgPlaceholderById(id);
    }

    /**
     * 构造占位符树,按类型分组
     *
     * @param msgPlaceholderTypeId
     * @return List<MsgPlaceholderType>
     */
    @Override
    public List<MsgPlaceholderType> selectMsgPlaceholderTree(String msgPlaceholderTypeId) {

        logger.info("开始加载占位符类型树");
        Map<String, List<MsgPlaceholder>> listMap = new LinkedHashMap<String, List<MsgPlaceholder>>(16);

        List<MsgPlaceholderType> msgPlaceholderTypeList = msgPlaceholderTypeMapper.selectByGroup();
        if (!CollectionUtils.isEmpty(msgPlaceholderTypeList)) {
            msgPlaceholderTypeList.stream().forEach(item -> {
                List<MsgPlaceholder> list = new ArrayList<MsgPlaceholder>(16);
                String msgPlaceholderNames = item.getMsgPlaceholderNames();
                String[] arr = msgPlaceholderNames.split("_");
                if (arr != null && arr.length > 0) {
                    String[] names = arr[0].split(",");
                    String[] notes = arr[1].split(",");
//                    int nameLen=names.length;
//                    int noteLen=notes.length;
//                    //处理note存在空值的情况，长度不一致
                    for (int i = 0; i < names.length; i++) {
                        MsgPlaceholder msgPlaceholder = new MsgPlaceholder();
                        msgPlaceholder.setName(names[i]);
                        msgPlaceholder.setNote(notes[i]);
//                         if(i<noteLen){
//                            msgPlaceholder.setNote(notes[i]+"_"+names[i]);
//                        }else{
//                            msgPlaceholder.setNote("_"+names[i]);
//                        }
                        list.add(msgPlaceholder);
                    }

                }
                item.setMsgPlaceholderList(list);
            });
            return msgPlaceholderTypeList;
        } else {
            return null;
        }
    }

    /**
     * 分页查询占位符
     *
     * @param query 实例
     * @return IPage<MsgPlaceholder>
     */
    @Override
    public IPage<MsgPlaceholder> getMsgPlaceholderList(MsgPlaceholderDto query) {
        return msgPlaceholderMapper.getMsgPlaceholderList(new Page(query.getPageNum(), query.getPageSize()), query);
    }

}
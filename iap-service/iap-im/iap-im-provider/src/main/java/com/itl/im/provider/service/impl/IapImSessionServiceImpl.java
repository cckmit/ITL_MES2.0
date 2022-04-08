/*
 * Copyright ? 2017 海通安恒科技有限公司.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.htah.com.cn/                            *
 *                                                                                     *
 ***************************************************************************************
 */
package com.itl.im.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.DtoUtils;
import com.itl.im.provider.core.netty.handler.CIMNioSocketAcceptor;
import com.itl.im.provider.mapper.IapImIMSessionMapper;
import com.itl.im.provider.util.SnowIdUtil;
import com.itl.im.provider.util.redis.SessionRedisTemplate;
import iap.im.api.entity.IapImSession;
import iap.im.api.sendDto.IapImSessionDto;
import iap.im.api.service.IapImIMSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * ImSession Service层实现类
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
@Service
public class IapImSessionServiceImpl extends ServiceImpl<IapImIMSessionMapper, IapImSession> implements IapImIMSessionService {

    @Resource
    private IapImIMSessionMapper iapImImSessionMapper;

    @Resource
    private SessionRedisTemplate sessionRedisTemplate;

    @Resource
    private CIMNioSocketAcceptor cimNioSocketAcceptor;

    @Value("#{'${server.host}'.trim()}")
    private String host;

    /**
     * 保存
     *
     * @param session
     */
    @Override
    public void save(IapImSessionDto session) {

        IapImSessionDto target = this.findSession(session.getAccount());

        if (target == null) {
            session.setId(SnowIdUtil.getLongId());
            iapImImSessionMapper.insert(DtoUtils.convertObj(session, IapImSession.class));
            sessionRedisTemplate.save(session);
            return;
        }

        target.setDeviceId(session.getDeviceId());
        target.setNid(session.getNid());
        target.setHost(session.getHost());
        target.setChannel(session.getChannel());
        target.setClientVersion(session.getClientVersion());
        target.setSystemVersion(session.getSystemVersion());
        target.setDeviceId(session.getDeviceId());
        target.setDeviceModel(session.getDeviceModel());

        target.setLatitude(session.getLatitude());
        target.setLocation(session.getLocation());
        target.setLongitude(session.getLongitude());
        target.setState(session.getState());
        target.setApns(session.getApns());
        target.setBindTime(session.getBindTime());

        this.saveOrUpdate(DtoUtils.convertObj(target, IapImSession.class));
        sessionRedisTemplate.save(target);
    }

    /**
     * 通过用户名（username）获取
     *
     * @param account username
     * @return IapImSessionDto
     */
    @Override
    public IapImSessionDto get(String account) {

        IapImSessionDto session = sessionRedisTemplate.get(account);
        if (session == null) {
            session = this.findSession(account);
            sessionRedisTemplate.saveOrRemove(account, session);
        }

        if (session != null && (Objects.equals(session.getHost(), host.trim()))) {
            session.setSession(cimNioSocketAcceptor.getManagedSession(session.getNid()));
        }

        return session;
    }

    /**
     * 根据account查找
     *
     * @param account 用户名（username)
     * @return IapImSessionDto
     */
    private IapImSessionDto findSession(String account) {
        IapImSession session = iapImImSessionMapper.selectOne(new QueryWrapper<IapImSession>().eq("account", account));
        if (session != null) {
            return DtoUtils.convertObj(session, IapImSessionDto.class);
        }
        return null;
    }

    /**
     * 获取在线用户列表
     *
     * @return List<String>
     */
    @Override
    public List<String> getOnlineAccountList() {
        return iapImImSessionMapper.findAccountList(IapImSessionDto.STATE_ENABLED);
    }

    /**
     * 移除
     *
     * @param account username
     */
    @Override
    public void remove(String account) {
        iapImImSessionMapper.delete(account);
        sessionRedisTemplate.delete(account);
    }


}

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
package iap.im.api.service;

import iap.im.api.sendDto.IapImSessionDto;

import java.util.List;

/**
 * 集群 session管理实现示例， 各位可以自行实现 AbstractSessionManager接口来实现自己的 session管理 服务器集群时
 * 须要将CIMSession 信息存入数据库或者redis中 等 第三方存储空间中，便于所有服务器都可以访问
 *
 * @author tanq
 * @date 2020-10-20
 * @since jdk1.8
 */
public interface IapImIMSessionService {

    /**
     * 保存
     *
     * @param session
     */
    void save(IapImSessionDto session);

    /**
     * 通过用户名（username）获取
     *
     * @param account username
     * @return IapImSessionDto
     */
    IapImSessionDto get(String account);

    /**
     * 获取在线用户列表
     *
     * @return List<String>
     */
    List<String> getOnlineAccountList();

    /**
     * 移除
     *
     * @param account username
     */
    void remove(String account);

}

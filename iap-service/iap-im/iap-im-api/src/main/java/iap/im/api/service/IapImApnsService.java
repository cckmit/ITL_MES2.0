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

import iap.im.api.dto.IapImMessageDto;

/**
 * 消息推送Service
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImApnsService {

    /**
     * 消息推送
     *
     * @param messageTemplate 消息
     * @param deviceToken     接收者token
     */
    void push(IapImMessageDto messageTemplate, String deviceToken);
}

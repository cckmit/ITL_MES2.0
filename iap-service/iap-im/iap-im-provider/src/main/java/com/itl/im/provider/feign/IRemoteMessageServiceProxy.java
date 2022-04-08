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
package com.itl.im.provider.feign;

import com.itl.iap.common.base.response.ResponseData;
import iap.im.api.dto.IapImMessageDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URI;

/**
 * 集群环境下消息转发实现
 * 目前使用了指定服务器ip来调用指定的服务器接口
 * 如果使用了spring cloud框架，建议自定义ribbon的负载均衡规则，更加优雅
 */
@Component
public class IRemoteMessageServiceProxy {
    //	@Value("${sys.message.dispatch.url}")
    private String url;

    @Resource
    private IRemoteMessageService messageRemoteService;

    public void forward(IapImMessageDto message, String ip) {
        ResponseData result = messageRemoteService.dispatch(URI.create(String.format(url, ip)), message);
//        message.setTimestamp(result.timestamp);
//        message.setId(result.id);
    }
}

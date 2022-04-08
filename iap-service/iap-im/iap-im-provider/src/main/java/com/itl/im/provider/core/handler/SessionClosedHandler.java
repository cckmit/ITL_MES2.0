/*
 * Copyright © 2017 海通安恒科技有限公司.
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
package com.itl.im.provider.core.handler;

import iap.im.api.service.IapImIMSessionService;
import iap.im.api.variable.CIMConstant;
import com.itl.im.provider.core.netty.handler.CIMRequestHandler;
import iap.im.api.sendDto.IapImSessionDto;
import iap.im.api.sendDto.SentBody;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 连接断开时，更新用户相关状态
 */
@Component
public class SessionClosedHandler implements CIMRequestHandler {

	@Resource
	private IapImIMSessionService iapImImSessionService;

	@Override
	public void process(IapImSessionDto ios, SentBody message) {

		Object quietly = ios.getAttribute(CIMConstant.KEY_QUIETLY_CLOSE);
		if (Objects.equals(quietly, true)) {
			return;
		}

		Object account = ios.getAttribute(CIMConstant.KEY_ACCOUNT);
		if (account == null) {
			return;
		}

		IapImSessionDto oldSession = iapImImSessionService.get(account.toString());

		if (oldSession == null || oldSession.isApnsEnable()) {
			return;
		}

		oldSession.setState(IapImSessionDto.STATE_DISABLED);
		oldSession.setNid(null);
		iapImImSessionService.save(oldSession);
	}

}

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
package com.itl.im.provider.core.annotation.impl;

import com.itl.im.provider.core.annotation.AccessToken;
import com.itl.im.provider.core.annotation.TokenAccount;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(TokenAccount.class)
				|| parameter.hasParameterAnnotation(AccessToken.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		if (parameter.hasParameterAnnotation(TokenAccount.class)) {
			String account = (String) webRequest.getAttribute("account", RequestAttributes.SCOPE_REQUEST);
			return account == null ? webRequest.getParameter("account") : account;
		}
		if (parameter.hasParameterAnnotation(AccessToken.class)) {
			return   webRequest.getAttribute("token", RequestAttributes.SCOPE_REQUEST);
		}

		return null;
	}

}

/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
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
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.itl.im.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import iap.im.api.entity.IapImSession;
import iap.im.api.sendDto.IapImSessionDto;

import java.util.List;

/**
 * IM Session Mapper
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImIMSessionMapper extends BaseMapper<IapImSession> {

	List<String> findAccountList(int state);
	IapImSessionDto findOne(String account);
	void delete(String account);
}

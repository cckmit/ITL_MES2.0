/*
 * Jomoo Omni Commerce Platform
 * <p>
 * Copyright (c) 2017-2018 Jomoo Group
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of Jomoo
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Jomoo.
 * <p>
 */
package com.itl.iap.mes.provider.common;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;

import java.util.Locale;
import java.util.Optional;

/**
 * Rest响应构造器
 * @author 胡广
 * @version 1.0
 * @name ResponseDTOBuilder
 * @description
 * @date 2019-07-0809:39
 */
public class ResponseDTOBuilder<T extends BaseResponse> {
	private MessageSource messageSource;

	private String code;
	private Object data;
    private Boolean success = true;
    private String message;
    private Long total;
    private Class<T> clazz;


	private ResponseDTOBuilder(Class<T> clazz) {
		this.clazz = clazz;
	}

	public static <T extends BaseResponse> ResponseDTOBuilder<T> with(Class<T> clazz) {
		return new ResponseDTOBuilder<T>(clazz);
	}

	public ResponseDTOBuilder<T> code(String code) {
		this.code = code;
		return this;
	}

	public ResponseDTOBuilder<T> data(Object data) {
        this.data = data;
        return this;
    }

    public ResponseDTOBuilder<T> total(Long total) {
        this.total = total;
        return this;
    }

    public ResponseDTOBuilder<T> success(Boolean success) {
        this.success = success;
        return this;
    }

    public ResponseDTOBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

	public T build() {
		BaseState state = new BaseState();
		state.setCode(code);
		state.setMessage(state.getMessage());
		try {

			T dto = clazz.newInstance();
			dto.setState(state);
            if(dto instanceof  PaginationResponse)
            {
                if(data instanceof Page)
                {
                    Page page = (Page)data;
                    PageableData pageableData = new PageableData();
                    pageableData.setTotal(page.getTotalElements());
                    ((PaginationResponse)dto).setPageableData(pageableData);
                    dto.setData(page.getContent());
                }
                if(data instanceof IPage)
                {
                    IPage page = (IPage)data;
                    PageableData pageableData = new PageableData();
                    pageableData.setTotal(page.getTotal());
                    ((PaginationResponse)dto).setPageableData(pageableData);
                    dto.setData(page.getRecords());
                }

                if(null != total) {
                    dto.setData(data);
                    PageableData pageableData = new PageableData();
                    pageableData.setTotal(total);
                    ((PaginationResponse)dto).setPageableData(pageableData);
                    ((PaginationResponse) dto).setPageableData(pageableData);
               }

            }else{
                dto.setData(data);
            }
            dto.setSuccess(Optional.ofNullable(success).orElse(Boolean.TRUE));
            return dto;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public T build(Exception e) {
		BaseState state= new BaseState();
		state.setCode(code);
		state.setMessage(message==null?e.getMessage():message);
        try {
			T dto = clazz.newInstance();
			dto.setState(state);
//            dto.setData(data);
            dto.setSuccess(Optional.ofNullable(success).orElse(Boolean.FALSE));
            return dto;
		} catch (InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	public String getMessageValue(final String key) {
		return messageSource.getMessage(key, null, Locale.CHINA);
	}

	public ResponseDTOBuilder<T> messageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
		return this;
	}

}

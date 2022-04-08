package com.itl.im.provider.core.config;

import com.itl.im.provider.core.handler.BindHandler;
import com.itl.im.provider.core.handler.SessionClosedHandler;
import com.itl.im.provider.core.netty.handler.CIMNioSocketAcceptor;
import com.itl.im.provider.core.netty.handler.CIMRequestHandler;
import iap.im.api.sendDto.IapImSessionDto;
import iap.im.api.sendDto.SentBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;

@Configuration
public class CIMConfig implements CIMRequestHandler, ApplicationListener<ApplicationStartedEvent> {

	@Resource
	private ApplicationContext applicationContext;

	private static final HashMap<String,Class<? extends CIMRequestHandler>> APP_HANDLER_MAP = new HashMap<>();

	static {
		/*
		 * 账号绑定handler
		 */
		APP_HANDLER_MAP.put("client_bind", BindHandler.class);
		/*
		 * 连接关闭handler
		 */
		APP_HANDLER_MAP.put("client_closed", SessionClosedHandler.class);
/*		*//*
		 * 上传定位handler
		 *//*
		APP_HANDLER_MAP.put("client_location", LocationHandler.class);
		*//*
		 * 修改信息handler
		 *//*
		APP_HANDLER_MAP.put("client_modify_profile", ModifyProfileHandler.class);
		*//*
		 * 修改头像handler
		 *//*
		APP_HANDLER_MAP.put("client_modify_logo", ModifyLogoHandler.class);*/
	}

	@Bean(destroyMethod = "destroy")
	public CIMNioSocketAcceptor getNioSocketAcceptor(@Value("${cim.app.port}") int port, @Value("${cim.web.port}") int webPort) {
		return new CIMNioSocketAcceptor.Builder()
		.setAppPort(port)
		.setWebsocketPort(webPort)
		.setOuterRequestHandler(this)
		.build();
	}

	@Override
	public void process(IapImSessionDto session, SentBody body) {
		
        CIMRequestHandler handler = findHandlerByKey(body.getKey());
		
		if(handler == null) {return ;}
		
		handler.process(session, body);
		
	}

	private CIMRequestHandler findHandlerByKey(String key){
		Class<? extends CIMRequestHandler> handlerClass = APP_HANDLER_MAP.get(key);
		if (handlerClass==null){
			return null;
		}
		return applicationContext.getBean(handlerClass);
	}


	/**
	 * springboot启动完成之后再启动cim服务的，避免服务正在重启时，客户端会立即开始连接导致意外异常发生.
	 */
	@Override
	public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
		applicationContext.getBean(CIMNioSocketAcceptor.class).bind();
	}
}
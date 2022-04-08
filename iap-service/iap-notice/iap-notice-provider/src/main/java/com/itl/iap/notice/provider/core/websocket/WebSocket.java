package com.itl.iap.notice.provider.core.websocket;

import com.itl.iap.notice.api.service.MsgSendRecordService;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author
 */
@EqualsAndHashCode
@Component
@ServerEndpoint("/noticeWebSocket/{userId}")
@Slf4j
public class WebSocket {
    private String currentUser;
    private Session session;
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private static ApplicationContext applicationContext;

    @Autowired
    private MsgSendRecordService msgSendRecordService;



    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {
        this.session = session;
        this.currentUser=userId;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());

        // 向前端发送未读个数
        if(msgSendRecordService == null){
            msgSendRecordService= applicationContext.getBean(MsgSendRecordService.class);
        }
        sendMessageTo(msgSendRecordService.getUnread(userId)+"",userId) ;
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    public void sendMessage(String message) {
        for (WebSocket webSocket: webSocketSet) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 发送给指定用户
     * @param message
     * @param userId
     * @throws IOException
     */
    public  void sendMessageTo(String message,String userId) throws IOException {
        for (WebSocket item : webSocketSet) {
            if(item.currentUser.equals(userId)){
                item.session.getBasicRemote().sendText(message);
            }
        }
    }

    public static void setApplicationContext(ApplicationContext applicationContext){
        WebSocket.applicationContext=applicationContext;
    }

}

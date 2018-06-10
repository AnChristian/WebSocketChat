package edu.njpi.rj1621.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录&聊天服务器
 * @author Fleming
 * 功能：不允许重复登录
 * 在工程发布时新建map，用于保存用户名，Session的键值对
 */
@WebListener
public class ChatServer implements ServletContextListener {

    private static ServletContext application;

    private static Map<String, Session> webSocketMap;

    public static ServletContext getApplication() {
        return application;
    }

    public static Map<String, Session> getWebSocketMap() {
        return webSocketMap;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        application = servletContextEvent.getServletContext();
        webSocketMap = new HashMap<>(16);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        application = null;
        webSocketMap = null;
    }
}

package edu.njpi.rj1621.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 登录服务器
 * @author Fleming
 * 功能：不允许重复登录
 */
@WebListener
public class LoginServer implements ServletContextListener {

    private static ServletContext application;

    public static ServletContext getApplication() {
        return application;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        application = servletContextEvent.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}

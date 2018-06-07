package edu.njpi.rj1621.action;

import com.google.gson.Gson;
import edu.njpi.rj1621.action.form.MessageDto;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fleming
 */
@Controller
@ServerEndpoint("/SocketHandle")
public class WebSocketHandle {

    private static int onlineCount = 0;
    //在线统计

    private static Map<String,WebSocketHandle> webSocketMap = new HashMap<String,WebSocketHandle>();

    private Session session;
    //每个用户的session

    //-------------------------------------------------------------------------------------------

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session + "建立连接");
    }

    //---------------------------------------------------------------------------------------------

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        System.out.println("退出");
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 服务器接收到客户端消息时调用的方法
     *
     * @param message   客户端发送过来的消息
     *
     * @param session   数据源客户端的session
     *
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    //---------------------------------------------------------------------------------------------

    /**
     * 发生错误时调用
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }


    //---------------------------------------------------------------------------------------------
    /**
     * 向所有在线用户发送在线人数
     *
     * @param message
     */
    public void sendOnlineCount(String message) {

    }

    //--------------------------------------------------------------------------------------------

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketHandle.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketHandle.onlineCount--;
    }

}
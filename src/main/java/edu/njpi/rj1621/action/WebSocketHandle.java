package edu.njpi.rj1621.action;

import com.google.gson.Gson;
import edu.njpi.rj1621.action.form.MessageDto;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fleming
 */
@Controller
@ServerEndpoint(value = "/SocketHandle/{param}")
public class WebSocketHandle {

    //在线统计
    private static int onlineCount = 0;


    private static Map<String,WebSocketHandle> webSocketMap = new HashMap();


    private Session session;
    //每个用户的session

    //-------------------------------------------------------------------------------------------

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     *
     * map里添加用户名和它的类
     * 通知前台在用户列表里添加用户
     * 通知前台更新在线人数
     */
    @OnOpen
    public void onOpen(@PathParam(value="param") String username, Session session) {
        System.out.println(session + "建立连接");
        this.session = session;
        System.out.println(username);
    }

    //---------------------------------------------------------------------------------------------

    /**
     * 连接关闭调用的方法
     *
     * 在线人数减一
     * 通知所有人前台更新用户人数
     * 用户map里减少用户
     * 通知所有人前台减少用户
     */
    @OnClose
    public void onClose() {
         subOnlineCount();
         sendOnlineCount(getOnlineCount()+"");
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 服务器接收到客户端消息时调用的方法
     *
     * @param message   客户端发送过来的消息
     *
     * @param session   数据源客户端的session
     *
     *  遍历用户map发送消息
     *  通知发送对象前台更新消息
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
        for (Map.Entry<String,WebSocketHandle> entry : webSocketMap.entrySet()) {
            try {
                entry.getValue().sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    //--------------------------------------------------------------------------------------------

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 在Map里移除记录，通知本人前台关闭聊天连接
     */
    public void removeSession(String username) throws IOException {
        Gson gson = new Gson();
        MessageDto message = new MessageDto();
        message.setData("关闭连接");
        message.setMessageType("onClose");
        webSocketMap.remove(username);
        sendMessage(gson.toJson(message));
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
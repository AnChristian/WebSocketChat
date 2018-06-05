package edu.njpi.rj1621.action;

import com.google.gson.Gson;
import edu.njpi.rj1621.action.form.MessageDto;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fleming
 */
@ServerEndpoint("/SocketHandle")
public class WebSocketHandle {

    private static int onlineCount = 0;
    //在线统计

    private static Map<String, WebSocketHandle> webSocketMap = new HashMap<>();
    //存放所有登录用户的Map集合，键：每个用户的唯一标识（用户名）

    private Session session;
    //httpSession用以在建立连接的时候获取登录用户的唯一标识（登录名）,获取到之后以键值对的方式存在Map对象里面

    private static HttpSession httpSession;

    public static void setHttpSession(HttpSession httpSession) {
        WebSocketHandle.httpSession = httpSession;
    }

    //-------------------------------------------------------------------------------------------

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {

        Gson gson = new Gson();
        this.session = session;
        webSocketMap.put((String) httpSession.getAttribute("username"), this);
        addOnlineCount();
        MessageDto md = new MessageDto();

        md.setMessageType("onlineCount");
        md.setData(onlineCount+"");

        sendOnlineCount(gson.toJson(md));
        System.out.println(getOnlineCount());
    }

    //---------------------------------------------------------------------------------------------

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        for (Map.Entry<String, WebSocketHandle> entry : webSocketMap.entrySet()) {
            if (entry.getValue().session == this.session) {
                webSocketMap.remove(entry.getKey());
                break;
            }
        }
        subOnlineCount();
        System.out.println(getOnlineCount());
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 服务器接收到客户端消息时调用的方法，（通过“@”截取接收用户的用户名）
     *
     * @param message   客户端发送过来的消息
     *
     * @param session   数据源客户端的session
     *
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        Gson gson = new Gson();
        System.out.println("收到客户端消息：" + message);
        StringBuffer messageStr = new StringBuffer(message);

        if (messageStr.indexOf("@") != -1) {
            String targetname = messageStr.substring(0, messageStr.indexOf("@"));
            String sourcename = "";
            for (Map.Entry<String,WebSocketHandle> entry  : webSocketMap.entrySet()) {



                //根据接收用户名遍历出接收对象
                if(targetname.equals(entry.getKey())){
                    //------------------------------------------------------------------------------
                    try {
                        for (Map.Entry<String, WebSocketHandle> entry1 : webSocketMap.entrySet()) {
                            //session在这里作为客户端向服务器发送信息的会话，用来遍历出信息来源
                            if (entry1.getValue().session == session) {
                                sourcename = entry1.getKey();
                            }
                        }
                        MessageDto md = new MessageDto();
                        md.setMessageType("message");
                        md.setData(sourcename+":"+message.substring(messageStr.indexOf("@")+1));
                        entry.getValue().sendMessage(gson.toJson(md));
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                    //---------------------------------------------------------------------------------
                }



            }
        }

    }

    //---------------------------------------------------------------------------------------------

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    //---------------------------------------------------------------------------------------------
    /**
     * 向所有在线用户发送在线人数
     *
     * @param message
     */
    public void sendOnlineCount(String message) {

        for (Map.Entry<String,WebSocketHandle> entry  : webSocketMap.entrySet()) {
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
        this.session.getBasicRemote().sendText(message);
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
package edu.njpi.rj1621.action;

import com.google.gson.Gson;
import edu.njpi.rj1621.action.form.MessageDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

/**
 * @author Fleming
 */
@Controller
@ServerEndpoint(value = "/SocketHandle/{param}")
@Component
public class WebSocketHandle {

    private static Map<String, Session> webSocketMap = ChatServer.getWebSocketMap();

    //-------------------------------------------------------------------------------------------

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     *
     * 查看webSocketMap里是否有username且值不等于参数，若有，移除之，通知其前台断开连接
     * map里添加用户名和它的Session
     *
     * 获取除了自己在线用户列表
     *
     * 通知除了自己前台在用户列表里添加用户
     */
    @OnOpen
    public void onOpen(@PathParam(value="param") String username, Session session) {

        Gson gson = new Gson();

        System.out.println(username+"建立连接");

        //若要就强制下线操作
        if(webSocketMap.containsKey(username) && webSocketMap.get(username) != session){
            MessageDto messageCloseConn = new MessageDto();
            messageCloseConn.setMessageType("CloseConn");
            webSocketMap.get(username).getAsyncRemote().sendText(gson.toJson(messageCloseConn));
            webSocketMap.remove(username);
        }

        //添加信息
        webSocketMap.put(username,session);

        //获取在线成员除了自己列表（发给自己）
        MessageDto messageDtoGetOnlineMember = new MessageDto();
        messageDtoGetOnlineMember.setMessageType("AddOnlineMember");
        for (Map.Entry<String, Session> entry : webSocketMap.entrySet()){
            if(!entry.getKey().equals(username)){
                messageDtoGetOnlineMember.setData(entry.getKey());
                webSocketMap.get(username).getAsyncRemote().sendText(gson.toJson(messageDtoGetOnlineMember));
            }
        }

        //通知其他前台更新在线列表（发给其他人）
        MessageDto messageDtoAddOnlineMember = new MessageDto();
        messageDtoAddOnlineMember.setMessageType("AddOnlineMember");
        messageDtoAddOnlineMember.setData(username);
        for (Map.Entry<String, Session> entry : webSocketMap.entrySet()) {
            if(!entry.getKey().equals(username)){
                entry.getValue().getAsyncRemote().sendText(gson.toJson(messageDtoAddOnlineMember));
            }
        }

    }

    //---------------------------------------------------------------------------------------------

    /**
     * 连接关闭调用的方法
     *
     * 用户map里减少用户
     * 在登录服务器application移除用户
     * 通知所有人前台减少用户
     */
    @OnClose
    public void onClose(@PathParam(value="param") String username) {
        Gson gson = new Gson();

        System.out.println(username+"断开连接");

        //移除map里关于username的记录
        webSocketMap.remove(username);

        //移除application关于username的记录
        ServletContext application = ChatServer.getApplication();
        application.removeAttribute(username);

        //通知所有前台更新在线列表（减少用户）
        MessageDto messageDtoRemoveOnlineMember = new MessageDto();
        messageDtoRemoveOnlineMember.setMessageType("RemoveOnlineMember");
        messageDtoRemoveOnlineMember.setData(username);
        for (Map.Entry<String, Session> entry : webSocketMap.entrySet()) {
            entry.getValue().getAsyncRemote().sendText(gson.toJson(messageDtoRemoveOnlineMember));
        }

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
        Gson gson = new Gson();

        String[] split = message.split("\\|");
        String receiverName = split[0];
        String messageFrom = split[1];
        String sourceName = "";

        for (Map.Entry<String, Session> entry : webSocketMap.entrySet()) {
            //根据接收用户名遍历出接收对象
            if (receiverName.equals(entry.getKey())) {

                //session在这里作为客户端向服务器发送信息的会话，用来遍历出信息来源
                for (Map.Entry<String, Session> entry1  : webSocketMap.entrySet()){
                    if (entry1.getValue() == session) {
                        sourceName = entry1.getKey();
                    }
                }

                MessageDto messageDto = new MessageDto();
                messageDto.setMessageType("message");
                messageDto.setData(sourceName+"|"+messageFrom);
                entry.getValue().getAsyncRemote().sendText(gson.toJson(messageDto));

            }
        }
    }

    //---------------------------------------------------------------------------------------------

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

}
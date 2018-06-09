package edu.njpi.rj1621.action;

import com.google.gson.Gson;
import edu.njpi.rj1621.action.form.MessageDto;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fleming
 */
@Controller
@ServerEndpoint(value = "/SocketHandle/{param}")
public class WebSocketHandle {

    private static Map<String,WebSocketHandle> webSocketMap = new HashMap();

    private Session session;
    //每个用户的session

    //-------------------------------------------------------------------------------------------

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     *
     * 查看application里是否有username，若有，移除之，通知其前台断开连接
     * map里添加用户名和它的类
     * 通知前台在用户列表里添加用户
     * 通知除了自己的前台更新在线人数
     */
    @OnOpen
    public void onOpen(@PathParam(value="param") String username, Session session) {

        Gson gson = new Gson();

        //重复登录之强制下线操作，下线后通知前台更新人数
        for (Map.Entry<String,WebSocketHandle> entry : webSocketMap.entrySet()) {
            if(username.equals(entry.getKey())){
                MessageDto messageClose = new MessageDto();
                messageClose.setMessageType("closeConn");
                //通知下线
                webSocketMap.get(username).session.getAsyncRemote().sendText(gson.toJson(messageClose));
                webSocketMap.remove(username);

                System.out.println("username："+username+"，session："+session+"  强制下线");
                continue;
            }
        }

        //建立连接
        System.out.println("用户名："+ username + "，session："+session+"  建立连接");
        this.session = session;
        webSocketMap.put(username,this);

        //通知所有前台更新在线人数
        MessageDto messageDtoOnlineCount = new MessageDto();
        messageDtoOnlineCount.setMessageType("onlineCount");
        messageDtoOnlineCount.setData(webSocketMap.size()+"");
        for (Map.Entry<String,WebSocketHandle> entry : webSocketMap.entrySet()) {
            entry.getValue().session.getAsyncRemote().sendText(gson.toJson(messageDtoOnlineCount));
        }

        //通知除了自己的前台更新在线用户列表
        MessageDto messageDtoOnlineMember = new MessageDto();
        messageDtoOnlineMember.setMessageType("AddOnlineMember");
        messageDtoOnlineMember.setData(username);
        for (Map.Entry<String,WebSocketHandle> entry : webSocketMap.entrySet()) {
            if (username.equals(entry.getKey())) {
                continue;
            } else {
                entry.getValue().session.getAsyncRemote().sendText(gson.toJson(messageDtoOnlineMember));
            }
        }

    }

    //---------------------------------------------------------------------------------------------

    /**
     * 连接关闭调用的方法
     *
     * 用户map里减少用户
     * 在线人数减一
     * 通知所有人前台更新用户人数
     * 通知所有人前台减少用户
     */
    @OnClose
    public void onClose(@PathParam(value="param") String username) {
        Gson gson = new Gson();

        webSocketMap.remove(username);

        System.out.println(username+"断开连接");

        //通知所有人前台更新用户人数
        MessageDto subOnlineCountM = new MessageDto();
        subOnlineCountM.setMessageType("onlineCount");
        subOnlineCountM.setData(webSocketMap.size()+"");
        for (Map.Entry<String,WebSocketHandle> entry : webSocketMap.entrySet()) {
            entry.getValue().session.getAsyncRemote().sendText(gson.toJson(subOnlineCountM));
        }

        //通知所有人前台减少用户
        MessageDto subOnlineMemberM = new MessageDto();
        subOnlineMemberM.setMessageType("RemoveOnlineMember");
        subOnlineMemberM.setData(username);
        for (Map.Entry<String,WebSocketHandle> entry : webSocketMap.entrySet()) {
            entry.getValue().session.getAsyncRemote().sendText(gson.toJson(subOnlineMemberM));
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

        //发送源
        String sourceName="";

        for (Map.Entry<String,WebSocketHandle> entry : webSocketMap.entrySet()) {
            if (receiverName.equals(entry.getKey())) {
                for (Map.Entry<String,WebSocketHandle> entry1  : webSocketMap.entrySet()) {
                    //session在这里作为客户端向服务器发送信息的会话，用来遍历出信息来源
                    if(entry1.getValue().session==session){
                        sourceName=entry1.getKey();
                    }
                }

                MessageDto messageDto = new MessageDto();
                messageDto.setMessageType("message");
                messageDto.setData(sourceName+"|"+messageFrom);

                entry.getValue().session.getAsyncRemote().sendText(gson.toJson(messageDto));

            }
        }
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

}
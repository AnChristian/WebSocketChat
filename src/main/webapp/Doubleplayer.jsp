<%--
  Created by IntelliJ IDEA.
  User: Fleming
  Date: 2018/6/3/003
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>双人聊天</title>
</head>
<body>
<body>
${username}<br/>
在线人数:<div id="onlineCount">0</div>
<br />
发送对象:<input id="username" type="text" width="50px"/>内容:<input id="text" type="text" />
<button onclick="send()">发送消息</button>
<hr />
<button onclick="closeWebSocket()">关闭WebSocket连接</button>
<hr />
<div id="message"></div>
</body>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: Fleming
  Date: 2018/6/3/003
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WebSocketChat</title>
    <style>
        *{
            margin:0;
        }
        body{
            background: url("img/bc1.jpg") no-repeat 50%;
            background-size: cover;
        }
        body,html{
            height: 100%;
        }
        #Chatbox{
            position: absolute;
            top: 50%;
            left: 50%;
            margin-left: -150px;
            margin-top: -270px;
            border-radius: 4px;
            -moz-border-radius: 4px;
            -webkit-border-radius: 4px;
            background-color: #fff;
            width: 430px;
            height: 540px;
            box-shadow: 0 2px 10px #999;
            -moz-box-shadow: #999 0 2px 10px;
            -webkit-box-shadow: #999 0 2px 10px;
        }
        #Chatbox2{
            position: absolute;
            top: 50%;
            left: 50%;
            margin-left: -350px;
            margin-top: -270px;
            border-radius: 4px;
            -moz-border-radius: 4px;
            -webkit-border-radius: 4px;
            background-color: #2e3238;
            width: 200px;
            height: 540px;
            box-shadow: 0 2px 10px #999;
            -moz-box-shadow: #999 0 2px 10px;
            -webkit-box-shadow: #999 0 2px 10px;
        }
        #UserSelf{
            width: 200px;
            height: 100px;
            text-align: center;
            font-size: 20px;
            line-height: 100px;
            color: #dedede;
        }
        #t2{
            margin-top: 10px;
            width: 200px;
            height: 30px;
            color: #999999;
            text-align: center;
        }
        #showMsg{
            width: 400px;
            height:380px;
            margin-left: 15px;
            border-bottom: 1px solid #999999;
        }
        #sendMsg{
            font-size: 24px;
            width: 400px;
            height:120px;
            margin-left: 15px;
            border: none;
            resize: none;
            outline: none;
        }
        #b1{
            height: 28px;
            width: 70px;
            margin-left: 300px;
        }
        #onlineUser{
            width: 200px;
            height: 330px;
            color: #ffffff;
            text-align: center;
            overflow: auto;
        }
    </style>
    <script type="text/javascript" src="webjars/jquery/3.0.0-alpha1/jquery.min.js"></script>
    <script>
        var websocket = null;

        if('WebSocket' in window) {
            websocket = new WebSocket("ws://localhost/SocketHandle");
        } else {
            alert("当前浏览器不支持WebSocket");
        }

        //连接错误的回调方法
        websocket.onerror = function () {
            setMessageInnerHTML("连接错误");
        }

        //连接成功的回调方法
        websocket.onopen = function () {
            setMessageInnerHTML("连接成功");
        }

        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            debugger
            var messageJson = eval("("+event.data+")");
            if (messageJson.messageType == "message") {
                document.getElementById("showMsg").innerHTML += messageJson.data;
            }
            if (messageJson.messageType == "onlineCount") {
                document.getElementById("onlineCount").innerHTML = "在线人数："+messageJson.data;
            }
        }

        //连接关闭的回调方法
        websocket.onclose = function () {
            closeWebSocket();
        }

        function closeWebSocket() {
            websocket.close();
        }
        
        function setMessageInnerHTML(message) {       //将内容显示在网页左上角
            document.getElementById('status').innerHTML = message;
        }

        function send() {

        }

    </script>
</head>
<body>
    <div id="Chatbox">

        <div id="showMsg">

        </div>
        <textarea id="sendMsg"></textarea>
        <button onclick="send()" id="b1">发送</button>
    </div>
    <div id="Chatbox2">
        <div id="UserSelf">${username }</div>

        <a id="onlineCount" style="margin-left: 55px; color: #999999">在线人数：</a>
        <div id="t2">在线成员</div>
        <div id="onlineUser">

        </div>
    </div>
    <div id="status" style="width: 120px;height: 20px"></div>
</body>
</html>

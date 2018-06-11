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
            font-size: 30px;
            width: 400px;
            height:380px;
            overflow: auto;
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
            font-size: 30px;
        }
        .onlineUserA:hover{
            cursor: pointer;
        }
    </style>
    <script type="text/javascript" src="webjars/jquery/3.0.0-alpha1/jquery.min.js"></script>
    <script>

        var websocket = null;
        var receiverName = null;

        if('WebSocket' in window) {
            websocket = new WebSocket("ws://localhost/SocketHandle/${username }");
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

            //收到消息时的动作
            if (messageJson.messageType == "message") {

                var split = messageJson.data.split("|");

                var myDate = new Date();
                document.getElementById("showMsg").innerHTML += split[0] +"："+ split[1] +"——"
                    + myDate.getHours() + "点"
                    + myDate.getMinutes() + "分"
                    +'<br/>';
            }

            //接收到增加在线成员时的动作
            if (messageJson.messageType == "AddOnlineMember") {
                addUser(messageJson.data);
            }

            //接收到移除在线成员时动作
            if (messageJson.messageType == "RemoveOnlineMember") {
                removeUser(messageJson.data);
            }

            //接收到强制下线的操作
            if(messageJson.messageType == "CloseConn"){
                websocket.close();
                setMessageInnerHTML("连接断开");
            }

        }

        //将内容显示在网页左上角
        function setMessageInnerHTML(message) {
            document.getElementById('status').innerHTML = message;
        }

        //退出时断开连接
        window.onbeforeunload = function () {
            websocket.close();
            return "退出将会断开连接";
        }

        //在对象列表上移除对象
        function removeUser(username) {
            var div = document.getElementById("onlineUser");
            var a = document.getElementsByClassName("onlineUserA");
            for(var i=0; i<a.length; i++){
                if (a[i].innerHTML == username) {
                    if(a[i].style.color == "rgb(255, 81, 81)"){
                        document.getElementById("b1").disabled = "disabled";
                    }
                    div.removeChild(a[i]);
                }
            }
        }

        //在对象列表上添加对象
        function addUser(username) {
            var suser = "'"+username+"'";
            var s = '<a class="onlineUserA" onclick="selectUser('+suser+')">'+username+'</a>';
            document.getElementById("onlineUser").innerHTML += s;
        }

        //选择用户名作为消息的发送对象，改变字体颜色，激活按钮
        function selectUser(username) {
            var a = document.getElementsByClassName("onlineUserA");
            for(var i = 0; i<a.length; i++){
                if(a[i].innerHTML == username){
                    if(a[i].style.color == "rgb(255, 81, 81)"){
                        a[i].style.color = "#ffffff";
                        receiverName = "";
                        document.getElementById("b1").disabled = "disabled";
                    } else {
                        a[i].style.color = "#FF5151";
                        receiverName = username;
                        document.getElementById("b1").disabled = false;
                    }
                }
            }
        }

        //发送消息
        function send() {
            if(receiverName == ""){
                alert("请选择你要发送的人");
            } else {
                var message = document.getElementById("sendMsg").value;
                if(message != ""){
                    var myDate = new Date();
                    document.getElementById("showMsg").innerHTML += "${username }" +"："+ message +"——"
                                                                                + myDate.getHours() + "点"
                                                                                + myDate.getMinutes() + "分"
                                                                                +'<br/>';
                    websocket.send(receiverName+"|"+message);
                    document.getElementById("sendMsg").value="";
                } else {
                    alert("请输入发送的内容");
                }
            }
        }

    </script>
</head>
<body>
    <div id="Chatbox">

        <div id="showMsg">

        </div>
        <textarea id="sendMsg"></textarea>
        <button onclick="send()" id="b1" disabled="disabled">发送</button>
    </div>
    <div id="Chatbox2">
        <div id="UserSelf">${username }</div>

        <div id="t2">选择发送对象</div><br/>
        <div id="onlineUser">

        </div>
    </div>
    <div id="status" style="width: 120px;height: 20px"></div>
</body>
</html>

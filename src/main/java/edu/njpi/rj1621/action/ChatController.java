package edu.njpi.rj1621.action;

import com.google.gson.Gson;
import edu.njpi.rj1621.action.form.Message;
import edu.njpi.rj1621.domain.User;
import edu.njpi.rj1621.service.UserSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Fleming
 */
@Controller
@RequestMapping("/Chat")
public class ChatController {

    @Resource
    private UserSvc userSvc;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/LoginAction.do", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
    @ResponseBody
    public String loginAction(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userSvc.queryUser(username);

        boolean success;
        if (user == null) {
            success = false;
        } else if (password.equals(user.getPassword())) {
            success = true;
        } else {
            success = false;
        }

        Message message;

        if (success) {
            HttpSession session = request.getSession();
            message = new Message("0","main.jsp");
            session.setAttribute("username",username);
            WebSocketHandle.setHttpSession(session);
        } else {
            message = new Message("1", "<h1>登陆失败</h1>");
        }

        Gson gson = new Gson();

        String json = gson.toJson(message);

        return json;
    }

    //-------------------------------------------------------------------------------------

    @RequestMapping(value = "/IsPassAction.do", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
    @ResponseBody
    public String ispassaction() {
        String username = request.getParameter("username");
        User user = userSvc.queryUser(username);
        boolean success = true;     //默认不存在该用户可以注册
        if (user != null) {         //若查询到用户，不可以注册
            success = false;        //不可以注册
        }

        Message message;

        if (success) {
            message = new Message("0");
        } else {
            message = new Message("1");
        }

        Gson gson = new Gson();

        String json = gson.toJson(message);

        return json;
    }

    //------------------------------------------------------------------------------------

    @RequestMapping(value = "/RegisterAction.do", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
    @ResponseBody
    public String registerAction() {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoleCode("yh");
        userSvc.addUser(user);

        Message message = new Message("0", "注册成功，点击右上角返回登录吧");

        Gson gson = new Gson();
        String json = gson.toJson(message);

        return json;
    }

}

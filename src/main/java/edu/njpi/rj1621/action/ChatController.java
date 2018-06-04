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

    @RequestMapping(value = "/LoginAction.do", method = RequestMethod.POST)
    @ResponseBody
    public String LoginAction() {

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
            message = new Message("0","main.jsp");
        } else {
            message = new Message("1", "<h1>登陆失败</h1>");
        }

        Gson gson = new Gson();

        String json = gson.toJson(message);

        return json;
    }



}

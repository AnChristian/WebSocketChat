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
import java.io.IOException;
import java.io.PrintWriter;

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
    public String ajaxDatas() {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userSvc.queryUser(username);

        boolean success = isSuccess(user, password);

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

//    private void renderData(HttpServletResponse response, Message message) {
//        PrintWriter printWriter = null;
//        Gson gson = new Gson();
//
//        String json = gson.toJson(message);
//
//        try {
//            printWriter = response.getWriter();
//            printWriter.print(json);
//        } catch (IOException ex) {
//            System.out.println("错误");
//        } finally {
//            if (null != printWriter) {
//                printWriter.flush();
//                printWriter.close();
//            }
//        }
//    }

    private boolean isSuccess(User user, String passWord) {

        if (user == null) {
            return false;
        } else if (passWord.equals(user.getPassword())) {
            return true;
        } else {
            return false;
        }

    }

}

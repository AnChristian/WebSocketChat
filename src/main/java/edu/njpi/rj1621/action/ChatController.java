package edu.njpi.rj1621.action;

import com.google.gson.Gson;
import edu.njpi.rj1621.action.form.Message;
import edu.njpi.rj1621.domain.User;
import edu.njpi.rj1621.service.UserSvc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Fleming
 */
@Controller
@RequestMapping("/Chat")
public class ChatController {

    @Resource
    private UserSvc userSvc;

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

        ServletContext application = LoginServer.getApplication();
        HttpSession session = request.getSession();

        System.out.println("正常登录——保存的SId："+application.getAttribute(username));
        System.out.println("正常登录——要登录的SId："+session.getId());


        //密码验证正确
        if (success) {
            //在别处登录
            if (application.getAttribute(username) != null && application.getAttribute(username) != session.getId()) {
                message = new Message("1", "账号已在其他地方登录，是否继续登录");

                //正常登录
            } else {
                message = new Message("0","main.jsp");
                session.setAttribute("username",username);
                application.setAttribute(username,session.getId());
            }

            //密码验证错误
        } else {
            System.out.println("密码错误");
            message = new Message("1", "");
        }

        Gson gson = new Gson();

        String json = gson.toJson(message);

        return json;
    }

    //-------------------------------------------------------------------------------------

    @RequestMapping(value = "/OverrideLoginAction.do", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
    @ResponseBody
    public String overrideLogin(HttpServletRequest request){
        String username = request.getParameter("username");

        ServletContext application = LoginServer.getApplication();
        HttpSession session = request.getSession();

        System.out.println("重复登录——保存的SId："+application.getAttribute(username));
        System.out.println("重复登录——要登录的SId："+session.getId());

        Message message = new Message("0","main.jsp");
        session.setAttribute("username",username);
        application.removeAttribute(username);
        application.setAttribute(username,session.getId());

        Gson gson = new Gson();

        String json = gson.toJson(message);

        return json;
    }

    //-------------------------------------------------------------------------------------

    @RequestMapping(value = "/IsPassAction.do", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
    @ResponseBody
    public String ispassaction(HttpServletRequest request) {
        String username = request.getParameter("username");
        User user = userSvc.queryUser(username);
        boolean success = true;
        //默认不存在该用户可以注册
        if (user != null) {
            //若查询到用户，不可以注册
            success = false;
            //不可以注册
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
    public String registerAction(HttpServletRequest request) {
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

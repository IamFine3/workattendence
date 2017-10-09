package com.coder520.login.Controller;

import com.coder520.common.utils.SecurityUtils;
import com.coder520.user.entity.User;
import com.coder520.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zrkj on 2017/7/29.
 */
@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping
    private String login() {
        return "login";
    }

    @RequestMapping("/check")
    @ResponseBody
    public String checkLogin(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String username = request.getParameter("username");
        String pwd = request.getParameter("password");

        User user = userService.findUserByUserName(username);

        if (user != null) {
           if (SecurityUtils.checkPassword(pwd, user.getPassword())) {
               // 登录成功
               // 设置session
               request.getSession().setAttribute("userinfo", user);
               return "login_succ";
           } else {
               // 校验失败 返回失败signal
               return "login_fail";
           }

        } else {
            // 校验失败 返回失败signal
            return "login_fail";
        }

    }

    @RequestMapping("/register")
    public String register(@RequestBody  User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        userService.createUser(user);

        return "succ";

    }


}
package com.coder520.user.Controller;

import com.coder520.user.entity.User;
import com.coder520.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zrkj on 2017/7/29.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String user() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        User user = new User();
        user.setUsername("coder");
        user.setPassword("coder520");
        user.setRealName("coder");
        user.setMobile("12343576");
        userService.createUser(user);
        return "user";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/userinfo")
    @ResponseBody
    public User getUser (HttpSession session) {
        User user = (User) session.getAttribute("userinfo");
        return user;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

}
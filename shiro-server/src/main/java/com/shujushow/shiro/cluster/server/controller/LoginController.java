package com.shujushow.shiro.cluster.server.controller;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xinshu on 2016/5/26.
 */
//@RestController
public class LoginController {

    @RequestMapping(value="/login")
    public String loginForm(HttpServletRequest request, Model model){
        String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
        String error = null;
        if(UnknownAccountException.class.getName().equals(exceptionClassName)){
            error = "用户名/密码错误";
        } else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)){
            error = "用户名/密码错误";
        }else if(exceptionClassName != null){
            error = "其它错误" + exceptionClassName;
        }
        model.addAttribute("error", error);
        return "/login.html";
    }
}

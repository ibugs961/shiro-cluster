package com.shujushow.shiro.cluster.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xinshu on 2016/5/28.
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "success";
    }
}

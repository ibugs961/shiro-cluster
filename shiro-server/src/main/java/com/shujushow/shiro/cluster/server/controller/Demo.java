package com.shujushow.shiro.cluster.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xinshu on 2016/5/25.
 */
@RestController
@RequestMapping("/")
public class Demo {

    @RequestMapping("/test")
    public Object test(){
        return "Hello world";
    }

}

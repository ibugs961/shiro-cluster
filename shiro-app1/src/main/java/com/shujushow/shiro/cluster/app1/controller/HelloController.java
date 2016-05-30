package com.shujushow.shiro.cluster.app1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xinshu on 2016/5/28.
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "app success";
    }
}

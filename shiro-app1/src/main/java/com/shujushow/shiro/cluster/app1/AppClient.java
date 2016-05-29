package com.shujushow.shiro.cluster.app1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by xinshu on 2016/5/29.
 */
@SpringBootApplication()
@ComponentScan
public class AppClient extends WebMvcConfigurerAdapter {

    public static void main(String[] args){
        SpringApplication application = new SpringApplication(AppClient.class);
        application.setWebEnvironment(true);
        application.run(args);
    }

}

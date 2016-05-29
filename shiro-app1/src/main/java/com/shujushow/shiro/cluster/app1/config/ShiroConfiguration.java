package com.shujushow.shiro.cluster.app1.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by xinshu on 2016/5/29.
 */
@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(){

        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();

        filterFactoryBean.setSecurityManager(securityManager());

        filterFactoryBean.setSuccessUrl("/hello");

        Map<String, String> chainDefinitionMap = filterFactoryBean.getFilterChainDefinitionMap();
        chainDefinitionMap.put("/*","authc");

        return filterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        return securityManager;
    }
}

package com.shujushow.shiro.cluster.app1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by xinshu on 2016/5/29.
 */
@Configuration
@ImportResource({"classpath:client/spring-client.xml"})
public class ShiroConfiguration {

//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(){
//
//        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
//
//        filterFactoryBean.setSecurityManager(securityManager());
//
//        filterFactoryBean.setSuccessUrl("/hello");
//
//        Map<String, String> chainDefinitionMap = filterFactoryBean.getFilterChainDefinitionMap();
//        chainDefinitionMap.put("/*","authc");
//
//        return filterFactoryBean;
//    }
//
//    @Bean
//    public DefaultWebSecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//
//        return securityManager;
//    }
}

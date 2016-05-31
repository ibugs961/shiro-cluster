package com.shujushow.shiro.cluster.app1.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by xinshu on 2016/5/29.
 */
@Configuration
@ImportResource({"classpath:client/spring-client.xml"})
@ComponentScan("com.shujushow.shiro")
public class ShiroConfiguration {

    @Bean
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

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

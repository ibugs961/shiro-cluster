package com.shujushow.shiro.cluster.app2.config;

import com.shujushow.shiro.cluster.client.ClientAuthenticationFilter;
import com.shujushow.shiro.cluster.client.ClientRealm;
import com.shujushow.shiro.cluster.client.ClientSessionDao;
import com.shujushow.shiro.cluster.client.ClientShiroFilterFactoryBean;
import com.shujushow.shiro.cluster.core.remote.RemoteServiceInterface;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.Map;

/**
 * Created by xinshu on 2016/5/29.
 */
@Configuration
@ComponentScan("com.shujushow.shiro")
public class ShiroConfiguration {

    @Value("${client.filters}")
    private String filtersStr;

    @Value("${client.filter.chain.definitions}")
    private String filterChainDefinitionsStr;

    @Value("${client.app.key}")
    private String appKey;

//    @Value("${client.remote.service.url}")
    String serviceUrl = "http://localhost/portal/remoteService";

    @Value("${client.rememberMe.id}")
    String rememberMeId;

    @Value("${client.session.id}")
    String clientSessionId;

    @Resource(name="remoteService")


    @Bean
    public HttpInvokerProxyFactoryBean remoteService(){
        HttpInvokerProxyFactoryBean invokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        invokerProxyFactoryBean.setServiceUrl(serviceUrl);
        invokerProxyFactoryBean.setServiceInterface(RemoteServiceInterface.class);
        return invokerProxyFactoryBean;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(){

        ClientShiroFilterFactoryBean filterFactoryBean = new ClientShiroFilterFactoryBean();

        filterFactoryBean.setSecurityManager(securityManager());

        filterFactoryBean.setSuccessUrl("/hello.html");

        Map<String, Filter> filters = filterFactoryBean.getFilters();
        filters.put("authc", clientAuthenticationFilter());


//        Map<String, String> chainDefinitionMap = filterFactoryBean.getFilterChainDefinitionMap();
//        chainDefinitionMap.put("/*","authc");
//
        filterFactoryBean.setFiltersStr(filtersStr);
        filterFactoryBean.setFilterChainDefinitionsStr(filterChainDefinitionsStr);

        return filterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(remoteRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public ClientRealm remoteRealm(){
        ClientRealm clientRealm = new ClientRealm();
        clientRealm.setCachingEnabled(false);
        clientRealm.setAppKey(appKey);
        clientRealm.setRemoteService((RemoteServiceInterface)remoteService());
        return clientRealm;

    }

    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setDeleteInvalidSessions(false);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionDAO(sessionDao());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }

    @Bean
    public ClientSessionDao sessionDao(){
        ClientSessionDao clientSessionDao = new ClientSessionDao();
        clientSessionDao.setSessionIdGenerator(sessionIdGenerator());
        clientSessionDao.setAppKey(appKey);
        clientSessionDao.setRemoteService((RemoteServiceInterface)remoteService());
        return clientSessionDao;
    }
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    @Bean
    public SimpleCookie sessionIdCookie(){
        SimpleCookie cookie = new SimpleCookie(clientSessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setDomain("");
        cookie.setPath("/");
        return cookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(){

        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        rememberMeManager.setCookie(rememberMeCookie());
        return rememberMeManager;
    }

    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie cookie = new SimpleCookie(rememberMeId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(2592000);
        cookie.setDomain("");
        cookie.setPath("/");
        return cookie;
    }


    @Bean
    public ClientAuthenticationFilter clientAuthenticationFilter(){
        ClientAuthenticationFilter filter = new ClientAuthenticationFilter();
        return filter;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return  new LifecycleBeanPostProcessor();
    }

    @Bean
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}

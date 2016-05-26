package com.shujushow.shiro.cluster.server.config;

import com.shujushow.shiro.cluster.server.credentials.RetryLimitHashedCredentialsMatcher;
import com.shujushow.shiro.cluster.server.realm.UserRealm;
import com.shujushow.shiro.cluster.server.session.dao.MySqlSessionDAO;
import com.shujushow.shiro.cluster.server.session.scheduler.MySqlSessionValidationScheduler;
import com.shujushow.shiro.cluster.server.shiro.filter.ServerFormAuthenticationFilter;
import com.shujushow.shiro.cluster.server.shiro.filter.SysUerFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xinshu on 2016/5/26.
 */
@Configuration
@ComponentScan(basePackages = "com.shujushow.shiro.cluster.server")
public class ShiroConfiguration {

    private static Map<String, String> filterChainMap = new LinkedHashMap<>();

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,
                                              FormAuthenticationFilter formAuthenticationFilter,
                                              SysUerFilter sysUserFilter) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        factoryBean.setLoginUrl("/login.html");
        Map<String, Filter> filters = factoryBean.getFilters();
        filters.put("authc", formAuthenticationFilter);
        filters.put("sysUser", sysUserFilter);

        filterChainMap.put("/remoteService", "anon");
        filterChainMap.put("/login", "authc");
        filterChainMap.put("/logout", "logout");
        filterChainMap.put("/authenticated", "authc");
        filterChainMap.put("/**", "user, sysUser");

        factoryBean.setFilterChainDefinitionMap(filterChainMap);
        return factoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();

        userRealm.setCredentialsMatcher(credentialsMatcher());
        userRealm.setCachingEnabled(false);

        return userRealm;
    }

    @Bean
    public RetryLimitHashedCredentialsMatcher credentialsMatcher() {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager());
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return ehCacheManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);

        MySqlSessionValidationScheduler scheduler = new MySqlSessionValidationScheduler();
        scheduler.setInterval(1800000);
        scheduler.setSessionManager(sessionManager);

        sessionManager.setSessionValidationScheduler(scheduler);
        sessionManager.setSessionDAO(mySqlSessionDAO());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(simpleCookie());
        return sessionManager;
    }

    @Bean
    MySqlSessionDAO mySqlSessionDAO() {
        MySqlSessionDAO mySqlSessionDAO = new MySqlSessionDAO();
        mySqlSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");

        mySqlSessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        return mySqlSessionDAO;
    }

    @Bean
    SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        simpleCookie.setDomain("");
        simpleCookie.setPath("/");
        return simpleCookie;
    }

    @Bean
    CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        rememberMeManager.setCookie(rememberMeCookie());

        return rememberMeManager;
    }

    @Bean
    SimpleCookie rememberMeCookie() {
        SimpleCookie rememberMe = new SimpleCookie("rememberMe");
        rememberMe.setHttpOnly(true);
        rememberMe.setMaxAge(2592000);
        rememberMe.setDomain("");
        rememberMe.setPath("/");

        return rememberMe;
    }


    @Bean
    public FormAuthenticationFilter formAuthenticationFilter() {

        ServerFormAuthenticationFilter authenticationFilter = new ServerFormAuthenticationFilter();
        authenticationFilter.setUsernameParam("username");
        authenticationFilter.setPasswordParam("password");
        authenticationFilter.setRememberMeParam("rememberMe");

        return authenticationFilter;
    }

    @Bean
    public SysUerFilter sysUserFilter() {
        return new SysUerFilter();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean invokingFactoryBean = new MethodInvokingFactoryBean();

        invokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        invokingFactoryBean.setArguments(new Object[]{securityManager()});

        return invokingFactoryBean;
    }

}

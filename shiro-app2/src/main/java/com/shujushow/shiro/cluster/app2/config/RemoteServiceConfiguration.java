package com.shujushow.shiro.cluster.app2.config;

import com.shujushow.shiro.cluster.core.remote.RemoteServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * Created by xinshu on 2016/5/31.
 */
@Configuration
@ComponentScan("com.shujushow.shiro")
public class RemoteServiceConfiguration {

    @Value("${client.remote.service.url}")
    String serviceUrl;

    @Bean(name="remoteService")
    public HttpInvokerProxyFactoryBean remoteService(){
        HttpInvokerProxyFactoryBean invokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        invokerProxyFactoryBean.setServiceUrl(serviceUrl);
        invokerProxyFactoryBean.setServiceInterface(RemoteServiceInterface.class);
        return invokerProxyFactoryBean;
    }
}

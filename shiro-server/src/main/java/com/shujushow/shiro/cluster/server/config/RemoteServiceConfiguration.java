package com.shujushow.shiro.cluster.server.config;

import com.shujushow.shiro.cluster.core.remote.RemoteServiceInterface;
import com.shujushow.shiro.cluster.server.remote.RemoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * Created by xinshu on 2016/5/26.
 */
@Configuration
@ComponentScan(basePackages = "com.shujushow.shiro.cluster.server.remote")
public class RemoteServiceConfiguration {

    @Bean(name="/remoteService")
    public HttpInvokerServiceExporter serviceExporter(RemoteService remoteService){

        HttpInvokerServiceExporter serviceExporter = new HttpInvokerServiceExporter();

        serviceExporter.setService(remoteService);
        serviceExporter.setServiceInterface(RemoteServiceInterface.class);

        return serviceExporter;
    }

}

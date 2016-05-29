package com.shujushow.shiro.cluster.client;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;

/**
 * Created by xinshu on 2016/5/28.
 */
public class ClientShiroFilterFactoryBean extends ShiroFilterFactoryBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setFiltersStr(String filters){
        if(StringUtils.isEmpty(filters)){
            return;
        }
        String[] filterArray = filters.split(";");
        for(String filter : filterArray){
            String[] filterKeyValue = filter.split("=");
            getFilters().put(filterKeyValue[0], (Filter)applicationContext.getBean(filterKeyValue[1]));
        }
    }

    public void setFilterChainDefinitionsStr(String filterChainDefinitions){
        if(StringUtils.isEmpty(filterChainDefinitions)){
            return;
        }
        String[] chainDefinitionsArray = filterChainDefinitions.split(";");
        for(String filter : chainDefinitionsArray){
            String[] filterKeyValue = filter.split("=");
            getFilterChainDefinitionMap().put(filterKeyValue[0], filterKeyValue[1]);
        }

    }
}

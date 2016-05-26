package com.shujushow.shiro.cluster.server.shiro.filter;

import com.shujushow.shiro.cluster.server.Constants;
import com.shujushow.shiro.cluster.server.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by xinshu on 2016/5/26.
 */
public class SysUerFilter extends PathMatchingFilter {

    @Autowired
    private UserService userService;

    protected boolean onPreHandle(ServletRequest request, ServletResponse response) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        request.setAttribute(Constants.CURRENT_USER, userService.findByUsername(username));
        return true;
    }

}

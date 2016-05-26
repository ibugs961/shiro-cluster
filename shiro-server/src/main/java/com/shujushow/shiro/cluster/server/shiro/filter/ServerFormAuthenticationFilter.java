package com.shujushow.shiro.cluster.server.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by xinshu on 2016/5/26.
 */
public class ServerFormAuthenticationFilter extends FormAuthenticationFilter {

    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        String fallbackUrl = (String) getSubject(request, response).getSession().getAttribute("authc.fallbackUrl");
        if (StringUtils.isEmpty(fallbackUrl)) {
            fallbackUrl = getSuccessUrl();
        }

        WebUtils.redirectToSavedRequest(request, response, fallbackUrl);
    }

}

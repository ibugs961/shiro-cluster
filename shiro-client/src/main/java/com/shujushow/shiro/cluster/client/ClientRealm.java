package com.shujushow.shiro.cluster.client;

import com.shujushow.shiro.cluster.core.remote.PermissionContext;
import com.shujushow.shiro.cluster.core.remote.RemoteServiceInterface;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by xinshu on 2016/5/28.
 */
public class ClientRealm extends AuthorizingRealm {

    private RemoteServiceInterface remoteService;
    private String appKey;


    public void setRemoteService(RemoteServiceInterface remoteService) {
        this.remoteService = remoteService;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        PermissionContext permissionContext = remoteService.getPermissions(appKey, username);
        authorizationInfo.setRoles(permissionContext.getRoles());
        authorizationInfo.setStringPermissions(permissionContext.getPermissions());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {


        throw new UnsupportedOperationException("子系统不进行认证操作");
    }
}

package com.shujushow.shiro.cluster.server.realm;

import com.shujushow.shiro.cluster.server.Constants;
import com.shujushow.shiro.cluster.server.entity.User;
import com.shujushow.shiro.cluster.server.service.AuthorizationService;
import com.shujushow.shiro.cluster.server.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xinshu on 2016/5/26.
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        authorizationInfo.setRoles(authorizationService.findRoles(Constants.SERVER_APP_KEY, username));
        authorizationInfo.setStringPermissions(authorizationService.findPermissions(Constants.SERVER_APP_KEY, username));

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UnknownAccountException();
        }

        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException();
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                getName()
        );
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals){
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals){
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals){
        super.clearCache(principals);
    }

    // TODO
//    public void clearAllCachedAuthorizationInfo(){
//        getAuthenticationCache().clear();
//    }
//
//    public void clearAllCachedAuthenticationInfo(){
//        getAuthenticationCache().clear();
//    }
//    public void clearAllCache(){
//        clearAllCachedAuthenticationInfo();
//        clearAllCachedAuthorizationInfo();
//    }
}

package com.shujushow.shiro.cluster.server.remote;

import com.shujushow.shiro.cluster.core.remote.PermissionContext;
import com.shujushow.shiro.cluster.core.remote.RemoteServiceInterface;
import com.shujushow.shiro.cluster.server.service.AuthorizationService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by xinshu on 2016/5/26.
 */
@Service
public class RemoteService implements RemoteServiceInterface {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public Session getSession(String appKey, Serializable sessionId) {
        return sessionDAO.readSession(sessionId);
    }

    @Override
    public Serializable createSession(Session session) {
        return sessionDAO.create(session);
    }

    @Override
    public void updateSession(String appKey, Session session) {
        sessionDAO.update(session);
    }

    @Override
    public void deleteSession(String appKey, Session session) {
        sessionDAO.delete(session);
    }

    @Override
    public PermissionContext getPermissions(String appKey, String username) {
        PermissionContext permissionContext = new PermissionContext();
        permissionContext.setRoles(authorizationService.findRoles(appKey, username));
        permissionContext.setPermissions(authorizationService.findPermissions(appKey, username));

        return permissionContext;
    }
}

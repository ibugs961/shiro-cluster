package com.shujushow.shiro.cluster.core.remote;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by xinshu on 2016/5/25.
 */
public class PermissionContext implements Serializable {

    private Set<String> roles;
    private Set<String> permissions;


    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }


    public String toString() {
        return "PermissionContext{" +
                ",roles=" + roles +
                ", permissions=" + permissions +
                "}";
    }

}

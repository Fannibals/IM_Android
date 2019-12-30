package net.qiujuer.web.italker.push.service;

import net.qiujuer.web.italker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseService {
    /**
     * 通过 @Context 注解，使得 securityContext 被赋值
     * 具体的值为 拦截器
     * {@link net.qiujuer.web.italker.push.provider.AuthRequestProvider}
     * 中赋的值
     */

    @Context
    protected SecurityContext securityContext;

    protected User getSelf(){
        return (User) securityContext.getUserPrincipal();
    }
}

package net.qiujuer.web.italker.push.provider;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

@Provider
public class AuthRequestProvider implements ContainerRequestFilter {

    /**
     * 实现接口的过滤方法
     * @param requestContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        /**
         *    if it is the request for login OR register,
         *    just return, because the token is not needed here
         */

        if (relationPath.startsWith("account/login")
                || relationPath.startsWith("account/register")){
            return;
        }

        /**
         *    Get the first token from the request headers.
         */

        String token = requestContext.getHeaders().getFirst("token");

        // Returns only when the User is not null
        // hence, simplifies the check process

        // if can find a user
        if (!Strings.isNullOrEmpty(token)){
            final User self = UserFactory.findByToken(token);
            if (self != null) {
                // 给当前请求添加上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        // user implements the principal interface
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        // role names
                        // is able to manage authorties
                        return true;
                    }

                    // Https?
                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                return;
            }
        }

        // if cannot find a user, then response an error
        // ResponseMdl
        ResponseModel model = ResponseModel.buildAccountError();


        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();
        // interceptor
        // return to service after doing this process
        requestContext.abortWith(response);

    }
}

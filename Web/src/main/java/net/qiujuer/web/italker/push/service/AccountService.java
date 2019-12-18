package net.qiujuer.web.italker.push.service;

import net.qiujuer.web.italker.push.bean.User;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.MessageDigest;

/**
 * @author Ethan
 */
@Path("/account")
public class AccountService {

    @GET
    @Path("/login")
    public String get(){
        return "You Get The Login";
    }

    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public User post(){
        User user = new User();
        user.setGender(1);
        user.setName("Kitty");
        return user;
    }
}

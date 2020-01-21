package net.qiujuer.web.italker.push.service;


import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.account.AccountRspModel;
import net.qiujuer.web.italker.push.bean.api.account.RegisterModel;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.api.user.UpdateInfoModel;
import net.qiujuer.web.italker.push.bean.card.UserCard;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;
import org.hibernate.sql.Update;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * @author Ethan Shin
 */
@Path("/user")
public class UserService extends BaseService{

    /**
     * 用户信息修改接口
     * @param token
     * @param model UpdateInfoModel
     * @return  用户个人信息
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(@HeaderParam("token") String token,
                                          UpdateInfoModel model){

        if (!UpdateInfoModel.check(model) || Strings.isNullOrEmpty(token)){
            return ResponseModel.buildParameterError();
        }

        User user = getSelf();
        // update the user's info
        user = model.updateToUser(user);
        user = UserFactory.update(user);

        UserCard card = new UserCard(user, true);
        return ResponseModel.buildOk(card);
    }
}

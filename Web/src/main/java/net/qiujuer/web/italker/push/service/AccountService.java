package net.qiujuer.web.italker.push.service;

import com.google.common.base.Strings;
import net.qiujuer.web.italker.push.bean.api.account.AccountRspModel;
import net.qiujuer.web.italker.push.bean.api.account.LoginModel;
import net.qiujuer.web.italker.push.bean.api.account.RegisterModel;
import net.qiujuer.web.italker.push.bean.api.base.ResponseModel;
import net.qiujuer.web.italker.push.bean.card.UserCard;
import net.qiujuer.web.italker.push.bean.db.User;
import net.qiujuer.web.italker.push.factory.UserFactory;
import sun.rmi.runtime.Log;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Ethan
 */
@Path("/account")
public class AccountService {

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model){

        if (!RegisterModel.check(model)){
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.findByPhone(model.getAccount());
        if (user != null) {
            // already have account
            return ResponseModel.buildHaveAccountError();
        }

        user = UserFactory.findByName(model.getName());
        if (user != null) {
            // already have name
            return ResponseModel.buildHaveNameError();
        }

        // register logic part
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),model.getName());

        if (user != null) {
            if (!Strings.isNullOrEmpty(model.getPushId())){
                return bind(user,model.getPushId());
            }
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        }else {
            return ResponseModel.buildRegisterError();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model){
        // 新建一个user的model
        User user = UserFactory.login(model.getAccount(),model.getPassword());

        // 检验参数
        if (!LoginModel.check(model)){
            // 如果参数不匹配，则返回参数异常
            return ResponseModel.buildParameterError();
        }

        if (user != null) {
            // 当model中pushId非空，则需要进行绑定
            if (!Strings.isNullOrEmpty(model.getPushId())){
                return bind(user,model.getPushId());
            }
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        }else {
            return ResponseModel.buildLoginError();
        }
    }

    // 绑定设备Id
    // PushId：唯一标识一台设备上的某一个应用
    @POST
    @Path("/bind/{pushId}")
    // 指定请求与返回的相应体为JSON
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    // 从请求头中获取token字段
    // pushId从url地址中获取
    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token,
                                               @PathParam("pushId") String pushId) {
        if (Strings.isNullOrEmpty(token) ||
                Strings.isNullOrEmpty(pushId)) {
            // 返回参数异常
            return ResponseModel.buildParameterError();
        }

        // 拿到自己的个人信息
        User user = UserFactory.findByToken(token);
//        User self = getSelf();
        if (user != null){
            return bind(user, pushId);
        }else{
            //Token失效，所以无法绑定
            return ResponseModel.buildAccountError();
        }
    }


    /**
     * 绑定的操作
     *
     * @param self   自己
     * @param pushId PushId
     * @return User
     */
    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        // 进行设备Id绑定的操作
        User user = UserFactory.bindPushId(self, pushId);

        if (user == null) {
            // 绑定失败则是服务器异常
            return ResponseModel.buildServiceError();
        }

        // 返回当前的账户, 并且已经绑定了
        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOk(rspModel);
    }
}

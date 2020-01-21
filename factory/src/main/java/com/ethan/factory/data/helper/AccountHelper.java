package com.ethan.factory.data.helper;

import android.text.TextUtils;

import com.ethan.factory.Factory;
import com.ethan.factory.Persistence.Account;
import com.ethan.factory.R;
import com.ethan.factory.data.DataSource;
import com.ethan.factory.model.api.AccountRspModel;
import com.ethan.factory.model.api.LoginModel;
import com.ethan.factory.model.api.RegisterModel;
import com.ethan.factory.model.api.RspModel;
import com.ethan.factory.model.db.AppDatabase;
import com.ethan.factory.model.db.User;
import com.ethan.factory.net.Network;
import com.ethan.factory.net.RemoteService;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountHelper {

    /**
     * 注册的接口，异步的调用
     *
     * @param model     传递一个注册的Model进来
     * @param callback  成功/失败的回调接口
     */
    public static void register(final RegisterModel model, final DataSource.Callback<User> callback){

        // 调用retrofit为我们的网络请求接口做代理
        RemoteService service = Network.remote();

        // get a call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);

        // Async request
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登录的调用
     *
     * @param model    登录的Model
     * @param callback 成功与失败的接口回送
     */
    public static void login(final LoginModel model, final DataSource.Callback<User> callback) {
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        // 异步的请求
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 对设备id的绑定操作
     *
     * @param callback
     */
    public static void bindPush(DataSource.Callback<User> callback){
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) return;

        // 调用retrofit对我们的网络请求做代理
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>>{
        final DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            // get the body
            RspModel<AccountRspModel> rspModel = response.body();

            if (rspModel.success()){
                AccountRspModel accountRspModel = rspModel.getResult();
                // 拿到我自己的信息
                User user = accountRspModel.getUser();
                // 进行数据库写入和缓存绑定

                // 第一种：直接保存
                user.save();
//                        // 第二种：通过ModelAdapter
//                        // 具体逻辑与第一种相同，不同在于更为复杂
//                        // 可以进行更多的操作，如save一个collection
//                        FlowManager.getModelAdapter(User.class).save(user);
//
//                        // 第三种：事务中
//                        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
//                        definition.beginTransactionAsync(new ITransaction() {
//                            @Override
//                            public void execute(DatabaseWrapper databaseWrapper) {
//                                FlowManager.getModelAdapter(User.class).save(user);
//                            }
//                        }).build().execute();

                Account.login(accountRspModel);

                // check is bind or not
                if (accountRspModel.isBind()){
                    Account.setBind(true);
                    if (callback!=null){ callback.onDataLoaded(user);}
                }else{
                    // bind this user to this device
                    bindPush(callback);
                }

            }else {
                // TODO:
                Factory.decodeRspCode(rspModel,callback);
                // callback.onDataNotAvailable();
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback!=null) callback.onDataNotAvailable(R.string.data_network_error);
        }
    }
}

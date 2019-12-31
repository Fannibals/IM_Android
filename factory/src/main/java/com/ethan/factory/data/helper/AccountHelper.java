package com.ethan.factory.data.helper;

import com.ethan.factory.Factory;
import com.ethan.factory.R;
import com.ethan.factory.data.DataSource;
import com.ethan.factory.model.api.AccountRspModel;
import com.ethan.factory.model.api.RegisterModel;
import com.ethan.factory.model.api.RspModel;
import com.ethan.factory.model.db.User;
import com.ethan.factory.net.Network;
import com.ethan.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountHelper {

    public static void register(final RegisterModel model, final DataSource.Callback<User> callback){

        // 调用retrofit为我们的网络请求接口做代理
        RemoteService service = Network.getRetrofit().create(RemoteService.class);

        // get a call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);

        // Async request
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {

                // get the body
                RspModel<AccountRspModel> rspModel = response.body();

                if (rspModel.success()){
                    AccountRspModel accountRspModel = rspModel.getResult();
                    // check is bind or not
                    if (accountRspModel.isBind()){
                        User user = accountRspModel.getUser();

                        // 进行数据库写入和缓存绑定
                        callback.onDataLoaded(user);
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
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });

    }

    /**
     * 对设备id的绑定操作
     *
     * @param callback
     */
    public static void bindPush(DataSource.Callback<User> callback){
        callback.onDataNotAvailable(R.string.app_name);
    }
}

package com.ethan.factory.net;

import com.ethan.factory.model.api.AccountRspModel;
import com.ethan.factory.model.api.RegisterModel;
import com.ethan.factory.model.api.RspModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求的所有的接口
 */
public interface RemoteService {

    /**
     * 网络请求一个注册接口
     * @param model
     * @return RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

}

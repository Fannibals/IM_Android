package com.ethan.factory.net;

import android.text.TextUtils;

import com.ethan.common.Common;
import com.ethan.factory.Factory;
import com.ethan.factory.Persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static Network instance;
    private Retrofit retrofit;
    private OkHttpClient client;

    static {
        instance = new Network();
    }

    private Network() {
    }

    public static Retrofit getRetrofit(){

        // 得到一个OK Client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // 拿到我们的请求
                        Request original = chain.request();
                        // 重新进行build
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            // 注入一个token
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        // 返回
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        instance.retrofit = builder.baseUrl(Common.constant.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return instance.retrofit;
    }

    /**
     * 返回一个请求代理
     * @return remote service
     */
    public static RemoteService remote(){
        return Network.getRetrofit().create(RemoteService.class);
    }
}

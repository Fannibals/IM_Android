package com.ethan.imapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ethan.factory.Factory;
import com.ethan.factory.Persistence.Account;
import com.ethan.factory.data.helper.AccountHelper;
import com.igexin.sdk.PushConsts;

/**
 * 个推的消息接收器
 */
public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        Bundle bundle = intent.getExtras();

        switch(bundle.getInt(PushConsts.CMD_ACTION)){
            case PushConsts.GET_CLIENTID:{
                Log.i(TAG,"GET_CLIENTID"+bundle.toString());
                // 当id初始化的时候，
                // 获取设备id
                onClientInit(bundle.getString("clientid"));
                break;
            }
            case PushConsts.GET_MSG_DATA:{
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    Log.i(TAG,"GET_MSG_DATA"+bundle.toString());

                    String message = new String(payload);
                    onMessageArrived(message);
                }
                break;
            }
            default:
                Log.i(TAG,"OTHERS: "+bundle.toString());
                break;
        }
    }

    /**
     * 当Id初始化的试试
     *
     * @param cid 设备Id
     */
    private void onClientInit(String cid) {
        // 设置设备Id
        Account.setPushId(cid);
        if (Account.isLogin()) {
            // 账户登录状态，进行一次PushId绑定
            // 没有登录是不能绑定PushId的
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息达到时
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        // 交给Factory处理
        Factory.dispatchPush(message);
    }
}

package com.ethan.factory.data;

import androidx.annotation.StringRes;

public interface DataSource {

    /**
     * 同时包括了成功与失败的回调接口
     * @param <T> 任意类型
     */
    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }

    /**
     * only focus on succeed call back
     * @param <T>
     */
    interface SucceedCallback<T>{
        // 数据加载成功，网络请求成功
        void onDataLoaded(T t);

    }

    /**
     * focus on failed cbs
     */

    interface FailedCallback{
        void onDataNotAvailable(@StringRes int strRes);
    }
}

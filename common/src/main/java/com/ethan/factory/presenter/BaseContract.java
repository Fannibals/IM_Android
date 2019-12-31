package com.ethan.factory.presenter;

import androidx.annotation.StringRes;

/**
 * MVP 模式中 公共的基本契约
 */
public interface BaseContract {
    interface View<T extends Presenter>{
        void showError(@StringRes int str);

        // public
        void showLoading();

        void setPresenter(T presenter);
    }

    interface Presenter{
        // public
        void start();
        // public
        void destroy();
    }
}

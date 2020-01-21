package com.ethan.factory.presenter.account;

import androidx.annotation.StringRes;

import com.ethan.factory.presenter.BaseContract;

public interface LoginContract {

    interface View extends BaseContract.View<Presenter>{
        void loginSuccess();

    }

    interface Presenter extends BaseContract.Presenter {
        // start a login
        void login(String phone, String password);
    }

}

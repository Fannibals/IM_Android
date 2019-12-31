package com.ethan.factory.presenter.account;

import androidx.annotation.StringRes;

import com.ethan.factory.presenter.BaseContract;

public interface LoginContract {

    interface View extends BaseContract.View<Presenter>{
        void loginSuccess();

        void showError(@StringRes int str);

        // public
        void showLoading();

        void setPresenter(RegisterContract.Presenter presenter);
    }

    interface Presenter extends BaseContract.Presenter {
        // start a login
        void login(String phone, String name, String password);
    }

}

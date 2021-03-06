package com.ethan.factory.presenter.account;


import androidx.annotation.StringRes;

import com.ethan.factory.presenter.BaseContract;

public interface RegisterContract {

    interface View extends BaseContract.View<Presenter> {
        void registerSuccess();
    }

    interface Presenter extends BaseContract.Presenter {

        // start a register
        void register(String phone, String name, String password);

        // check the validation of the mobile
        boolean checkMobile(String phone);
    }

}

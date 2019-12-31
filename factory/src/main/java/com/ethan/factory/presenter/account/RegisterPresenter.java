package com.ethan.factory.presenter.account;

import android.graphics.Paint;
import android.text.TextUtils;

import com.ethan.common.Common;
import com.ethan.factory.R;
import com.ethan.factory.data.DataSource;
import com.ethan.factory.data.helper.AccountHelper;
import com.ethan.factory.model.api.RegisterModel;
import com.ethan.factory.model.db.User;
import com.ethan.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {


    public RegisterPresenter(RegisterContract.View mView) {
        super(mView);
    }

    @Override
    public void register(String phone, String name, String password) {
        start();

        RegisterContract.View view = getView();


        if(!checkMobile(phone)){
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        }else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else {

            // create the model for request
            RegisterModel model = new RegisterModel(phone,password,name);
            // 进行网路请求，并设置回送接口为自己
            AccountHelper.register(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.constant.REGEX_MOBILE,phone);
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onDataLoaded(User user) {

        // 告知界面，注册成功
        final RegisterContract.View view = getView();

        if(view == null) return;

        // when back from the aync thread,
        // you are not sure whether it is in MainThread or not
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });


    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 告知界面，fail
        final RegisterContract.View view = getView();

        if(view == null) return;

        // when back from the aync thread,
        // you are not sure whether it is in MainThread or not
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}

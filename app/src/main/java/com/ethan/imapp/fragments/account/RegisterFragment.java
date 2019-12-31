package com.ethan.imapp.fragments.account;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ethan.common.app.BaseFragment;
import com.ethan.common.app.PresenterFragment;
import com.ethan.factory.presenter.account.RegisterContract;
import com.ethan.factory.presenter.account.RegisterPresenter;
import com.ethan.imapp.R;
import com.ethan.imapp.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {
    private AccountTrigger accountTrigger;

    public RegisterFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.edit_phone)
    EditText mPhone;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // get the ref of the
        accountTrigger = (AccountTrigger) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }


    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();

        // 调用P层进行注册
        mPresenter.register(phone,name,password);
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick(){
        // 进行界面切换
        accountTrigger.triggerView();
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();

        // enables the edit txts
        mPassword.setEnabled(false);
        mName.setEnabled(false);
        mPhone.setEnabled(false);
        // THINK: what is the dif bwn clickable and enable ?
        mSubmit.setEnabled(false);

    }

    @Override
    public void showError(int str) {
        super.showError(str);
        // stop the loading
        mLoading.stop();

        // enables the edit txts
        mPassword.setEnabled(true);
        mName.setEnabled(true);
        mPhone.setEnabled(true);
        // THINK: what is the dif bwn clickable and enable ?
        mSubmit.setEnabled(true);

    }

    @Override
    public void registerSuccess() {

        // 注册界面结束后，直接跳到登陆界面
        MainActivity.show(getContext());

        // 删除原本登录界面
        getActivity().finish();
    }
}

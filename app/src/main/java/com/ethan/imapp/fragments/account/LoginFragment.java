package com.ethan.imapp.fragments.account;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.common.app.BaseFragment;
import com.ethan.imapp.R;

/**
 * Log in Fragment
 **/
public class LoginFragment extends BaseFragment {

    private AccountTrigger accountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // get the ref of the
        accountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 进行一次切换，注册界面
        accountTrigger.triggerView();
    }
}

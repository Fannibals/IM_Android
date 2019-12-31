package com.ethan.common.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ethan.factory.presenter.BaseContract;

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter>
        extends BaseFragment implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initPresenter();
    }

    /**
     * 初始化一个presenter
     * @return 一个泛型的presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(int str) {
        MyApplication.showToast(str);
    }

    @Override
    public void showLoading() {
        // TODO: show a loading dialog
    }

    @Override
    public void setPresenter(Presenter presenter) {
        //
        mPresenter = presenter;
    }
}

package com.ethan.imapp.activities;

import android.content.Intent;

import com.ethan.common.app.BaseActivity;
import com.ethan.imapp.fragments.user.UpdateInfoFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ethan.imapp.R;

public class UserActivity extends BaseActivity {

    Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mCurFragment = new UpdateInfoFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();
    }

    /**
     * callback   when successfully tailors the photo
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }
}

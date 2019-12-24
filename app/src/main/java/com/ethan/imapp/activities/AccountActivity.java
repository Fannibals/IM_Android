package com.ethan.imapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ethan.common.app.BaseActivity;
import com.ethan.common.app.BaseFragment;
import com.ethan.imapp.R;
import com.ethan.imapp.fragments.UpdateInfoFragment;
import com.ethan.imapp.fragments.main.ContactFragment;
import com.yalantis.ucrop.UCrop;

public class AccountActivity extends BaseActivity {

    Fragment mCurFragment;
    /**
     * New an account activity
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }
}

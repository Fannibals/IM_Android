package com.ethan.imapp;

import android.os.Bundle;

import com.ethan.common.app.BaseActivity;
import com.ethan.imapp.activities.MainActivity;
import com.ethan.imapp.fragments.assist.PermissionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ( PermissionFragment.haveAll(this,getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }
}

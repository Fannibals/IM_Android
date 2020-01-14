package com.ethan.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在界面未初始化之前调用的初始化窗口
        initWindows();

        if (initArgs(getIntent().getExtras())){
            setContentView(getContentLayoutId());
            initWidget();
            initData();
        }else{
            finish();
        }
    }

    protected void initWindows(){

    }

    /**
     * init some arguments
     * @param bundle
     * @return
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    protected abstract int getContentLayoutId();

    /**
     * init the layout
     */
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        // when click the back btn on the navigation bar, finish itself
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * checking whether the customized fragment has intercept the back method
     * @see BaseFragment#onBackPressed()
     */
    @Override
    public void onBackPressed() {

        // get all fragments of current activity
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment: fragments){
                if (fragment instanceof BaseFragment){
                    if (((BaseFragment) fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }
}

package com.ethan.imapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.ethan.common.app.BaseActivity;
import com.ethan.factory.Persistence.Account;
import com.ethan.imapp.activities.AccountActivity;
import com.ethan.imapp.activities.MainActivity;
import com.ethan.imapp.fragments.assist.PermissionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends BaseActivity {

    // Drawable
    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // get the root view
        View root = findViewById(R.id.activity_launch);

        // get color
        int color = UiCompat.getColor(getResources(),R.color.colorPrimary);

        // create a color Drawable
        ColorDrawable drawable = new ColorDrawable(color);

        // set the drawable to the root view
        root.setBackground(drawable);
        mBgDrawable = drawable;

    }

    @Override
    protected void initData() {
        super.initData();

        // 动画进入到50%，等待pushId获取到
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });
    }

    private void waitPushReceiverId(){

        if(Account.isLogin()){
            if (Account.isBind()){
                skip();
                return;
            }
        }else{
            // 如果没有登陆， 没有登陆是不能进行绑定的
            // 如果拿到了PushId
            if(!TextUtils.isEmpty(Account.getPushId())){
                skip();
                return;
            }
        }

        // 循环等待
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        },500);

    }

    private void skip(){

        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                skipOpr();
            }
        });
    }

    /**
     * real skip operation
     */
    private void skipOpr(){
        if ( PermissionFragment.haveAll(this,getSupportFragmentManager())) {
            // check whether is directed to the home page or login page
            if(Account.isLogin()){
                MainActivity.show(this);
            }else {
                AccountActivity.show(this);
            }
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 给背景设置一个动画
     * @param endProgress   动画的结束进度
     * @param endCallback   动画结束时触发
     */
    private void startAnim(float endProgress, final Runnable endCallback){

        // 获取一个最终颜色
        int finalColor = Resource.Color.WHITE;

        // 运算当前进度的颜色
        //
        ArgbEvaluator evaluator = new ArgbEvaluator();

        // endProgress(fraction) endColor的比例
        // eg. float a = startA + fraction * (endA - startA);
        int endColor = (int) evaluator.evaluate(endProgress,mBgDrawable.getColor(),finalColor);

        // 构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endCallback);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(),finalColor);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });

        valueAnimator.start();


    }

    private Property<LaunchActivity,Object> property =
            new Property<LaunchActivity, Object>(Object.class,"color") {
        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }
    };
}






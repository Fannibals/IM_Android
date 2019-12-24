package com.ethan.imapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.ethan.common.app.BaseActivity;
import com.ethan.common.app.BaseFragment;
import com.ethan.common.widget.PortraitView;
import com.ethan.imapp.activities.AccountActivity;
import com.ethan.imapp.fragments.main.ActiveFragment;
import com.ethan.imapp.fragments.main.ContactFragment;
import com.ethan.imapp.fragments.main.GroupFragment;
import com.ethan.imapp.helper.NavHelper;
import com.ethan.imapp.helper.PermissionHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer>{

    @BindView(R.id.appBar)
    View mLayAppBar;

    @BindView(R.id.img_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    /**
     * Handling fragment
     */
    private NavHelper<Integer> mNavHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mNavHelper = new NavHelper(this, R.id.lay_container,
                getSupportFragmentManager(),this);
        mNavHelper.add(R.id.action_home,new NavHelper.Tab<>(ActiveFragment.class,R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class,R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));

        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppBar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                })
        ;

    }

    @Override
    protected void initData() {
        super.initData();

        // 从底部导航栏中接管我们的Menu，然后进行手动的触发第一次点击
        Menu menu = mNavigation.getMenu();
        // default: select home
        menu.performIdentifierAction(R.id.action_home,0);

    }

    @OnClick(R.id.img_search)
    void onSearchMenuClick(){

    }

    @OnClick({R.id.btn_action})
    void onActionClick(){
        AccountActivity.show(this);
    }

    /**
     * is Triggered when we click the navigation bar
     *
     * @param menuItem
     * @return  True for we can hold the action event
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // 转接事件流到工具类中
        return mNavHelper.performClickMenu(menuItem.getItemId());
    }


    /**
     * CallBack methods after NavHelper executed
     * @param newTab
     * @param oldTab
     */

    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        // 从额外字段
        mTitle.setText(newTab.extra);

        // Animation
        float transY = 0;
        float rotation = 0;

        if (newTab.extra.equals(R.string.title_home)){
            // hide the btn when at the homepage
            transY = Ui.dipToPx(getResources(),76);
        }else{
            //
            if (newTab.extra.equals(R.string.title_group)){
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            }else{
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }

        // start animation
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1)) // 弹性效果
                .setDuration(480)
                .start();
    }
}



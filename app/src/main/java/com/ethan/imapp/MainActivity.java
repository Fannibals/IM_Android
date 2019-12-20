package com.ethan.imapp;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.ethan.common.app.BaseActivity;
import com.ethan.common.app.BaseFragment;
import com.ethan.common.widget.PortraitView;
import com.ethan.imapp.fragments.main.ActiveFragment;
import com.ethan.imapp.fragments.main.ContactFragment;
import com.ethan.imapp.fragments.main.GroupFragment;
import com.ethan.imapp.helper.NavHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    }

    @OnClick(R.id.img_search)
    void onSearchMenuClick(){

    }

    @OnClick({R.id.btn_action})
    void onActionClick(){

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
    }
}



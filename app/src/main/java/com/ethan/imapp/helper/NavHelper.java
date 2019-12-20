package com.ethan.imapp.helper;

import android.content.Context;
import android.util.SparseArray;

import androidx.annotation.FloatRange;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ethan.common.app.BaseFragment;
import com.ethan.imapp.R;

/**
 * Reuse the fragment and allocation
 */
public class NavHelper<T> {

    // lightweight array than arraylist / linked list
    // array for all tabs
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();

    // init parameters
    private final Context context;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> listener;

    //current selected Tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager,
                     OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    public boolean performClickMenu(int menuId){
        // 集合中寻找点击的菜单对应的Tab
        // 如果有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab!=null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * real operation
     * @param tab
     */
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                notifyReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }

    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                // 从界面移除，但仍然在Fragment Cache中
                ft.detach(oldTab.fragment);
            }
        }

        if(newTab != null) {
            if (newTab.fragment == null) {
                Fragment fragment = Fragment.instantiate(context,newTab.clx.getName());
                // put in cache
                newTab.fragment = fragment;
                // push to FragmentManager
                ft.add(containerId,fragment,newTab.clx.getName());

            }else{
                ft.attach(newTab.fragment);
            }
        }

        ft.commit();
        notifyTabSelect(newTab,oldTab);

    }

    /**
     * call back the interface
     * @param newTab
     * @param oldTab
     */

    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab){
        if (listener != null) {
            listener.onTabChanged(newTab ,oldTab);
        }
    }

    private void notifyReselect(Tab<T> tab){
        //TODO: double click to refresh
    }


    /**
     * Fragment操作区别
     *  1-2 Add， replace
     *
     *  3-4 Hide/Show：纯粹的隐藏与显示，不移除
     *
     *  5-6 Attach/Detach：从布局上移除，但存储到缓存队列中，可重用
     *
     *  Remove 直接移除掉
     */

    /**
     * add tab
     * @param menuId
     * @param tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab){
        tabs.put(menuId,tab);
        return this;
    }

    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 我们的所有的Tab基础属性
     * @param <T> 泛型，额外属性
     */
    public static class Tab<T>{

        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        // Fragment 对应的 class info
        public Class<?> clx;
        public T extra;

        Fragment fragment;
    }

    /**
     * 定义事件处理完成之后的回调接口
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}

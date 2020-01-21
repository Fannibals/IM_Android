package com.ethan.factory.presenter;

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    private T mView;

    public BasePresenter(T mView) {
        setView(mView);
    }

    /**
     * 设置一个view，子类可以复写
     * @param view
     */
    protected void setView(T view){
        // set the view to presenter
        mView = view;
        // set view's presenter as well
        mView.setPresenter(this);
    }

    /**
     * 给子类使用的获取view的操作
     * @return
     */
    protected final T getView(){
        return mView;
    }


    @Override
    public void start() {
        T view = mView;
        // 在开始的时候进行loading调用
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            view.setPresenter(null);
        }
    }
}

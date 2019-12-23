package com.ethan.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends androidx.fragment.app.Fragment {

    protected Unbinder mRootUnbinder;
    protected View mRoot;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRoot == null) {
            int layId = getContentLayoutId();
            // init current
            View root = inflater.inflate(layId,container,false);
            initWidget(root);
            mRoot = root;
        }else{
            if (mRoot.getParent() != null) {
                // Remove the current root remove from it parent
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;

    }

    /**
     * It is recommended to only inflate the layout in onCreateView(...) and
     * move logic that operates on the returned View to onViewCreated(View, Bundle).
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    protected abstract int getContentLayoutId();

    /**
     * init some arguments
     * @param bundle
     * @return
     */
    protected void initArgs(Bundle bundle){
    }


    protected void initWidget(View root){
        mRootUnbinder = ButterKnife.bind(this,root);
    }

    protected void initData(){

    }

    /**
     * trigger when back btn is clicked
     * @return true if the logic has already been processed
     *          false if still not
     */
    public boolean onBackPressed(){
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mRootUnbinder.unbind();
    }
}

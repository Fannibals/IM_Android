package com.ethan.imapp.fragments.main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.common.app.BaseFragment;
import com.ethan.imapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends BaseFragment {


    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

}

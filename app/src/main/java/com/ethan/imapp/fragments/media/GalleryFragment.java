package com.ethan.imapp.fragments.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.ethan.common.app.BaseFragment;
import com.ethan.common.tools.UiTools;
import com.ethan.common.widget.GalleryView;
import com.ethan.imapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.qiujuer.genius.ui.Ui;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment
        implements GalleryView.SelectedChangeListener {

    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 我们先使用默认的
//        return new BottomSheetDialog(getContext());
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container,false);
        mGallery = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(LoaderManager.getInstance(this),this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if(count > 0) {
            // 隐藏自己
            dismiss();
            if (mListener != null) {
                String[] paths = mGallery.getSelectedPath();
                mListener.onSelectedImage(paths[0]);
                // 取消引用，加快gc
                mListener = null;
            }
        }

    }

    /**
     * 设置事件监听，返回自己
     * @param mListener
     */
    public GalleryFragment setListener(OnSelectedListener mListener) {
        this.mListener = mListener;
        return this;
    }

    /**
     * interface for selecting the image
     */
    public interface OnSelectedListener{
        void onSelectedImage(String path);
    }


    /**
     * 自定义SheetDialog： 为了使顶部状态栏不变色（不变成黑色）
     */
    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null) return;

            int screenHeight = UiTools.getScreenHeight(getOwnerActivity());
            int statusHeight = UiTools.getScreenHeight(getOwnerActivity());

            // calculate dialog height
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }
}

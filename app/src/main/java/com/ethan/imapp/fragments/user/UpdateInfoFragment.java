package com.ethan.imapp.fragments.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.RecoverySystem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ethan.common.app.BaseFragment;
import com.ethan.common.app.MyApplication;
import com.ethan.common.widget.PortraitView;
import com.ethan.factory.Factory;
import com.ethan.factory.net.UploadHelper;
import com.ethan.imapp.R;
import com.ethan.imapp.fragments.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Update User's Information
 */
public class UpdateInfoFragment extends BaseFragment {

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info ;
    }


    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                // 设置图片处理的格式：JPEG
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // 设置压缩后的图片精度
                options.setCompressionQuality(96);

                // 得到头像的缓存地址
                File dPath = MyApplication.getPortraitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1,1)       // 1：1的比例
                        .withMaxResultSize(520,520)     //返回的最大的参数
                        .withOptions(options)
                        .start((AppCompatActivity) getActivity());


            } // show的时候建议使用getChildFragmentManager
              // tag Gallery class name
        }).show(getChildFragmentManager(),GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 收到从Activity传递过来的回调，然后取出其中的值进行图片加载
        // 如果使我能够处理的类型
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }else if (resultCode == UCrop.RESULT_ERROR){
                final Throwable cropError = UCrop.getError(data);
            }
        }
    }

    /**
     * load uri to portrait
     * @param uri
     */
    private void loadPortrait(Uri uri) {
        Glide.with(getContext())
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

        final String localPath = uri.getPath();

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortrait(localPath);
                Log.e("TAG","url: "+ url);
            }
        });
    }

}

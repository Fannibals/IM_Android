package com.ethan.common.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ethan.common.R;
import com.ethan.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GalleryView extends RecyclerView {

    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3;
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024 ;
    private LoaderCallback mLoaderCallBack = new LoaderCallback();
    private Adapter mAdapter = new Adapter();
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectedChangeListener mSelectedListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                // Cell点击操作，如果说我们的点击是允许的，那么更新对应的Cell的状态
                // 然后更新界面，同理；如果说不能允许点击（已经达到最大的选中数量）那么就不刷新界面
                if (onItemSelectClick(image)) {
                    //noinspection unchecked
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     *
     * @param loaderManager
     * @return True, 代表我进行了数据更改，你需要刷新；反之不刷新
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener mSelectedListener) {
        this.mSelectedListener = mSelectedListener;
        loaderManager.initLoader(LOADER_ID,null,mLoaderCallBack);
        return LOADER_ID;
    }

    /**
     * Specific Logic of the Cell click
     * @param image Image
     * @return True for we've changed the data, need to refresh
     *         False for not.
     */
    private boolean onItemSelectClick(Image image){
        // 是否需要进行刷新
        boolean notifyRefresh = true;
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
            image.isSelect = false;
        }else{
            if(mSelectedImages.size() >= MAX_IMAGE_COUNT) {
                // Make a toast
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str,MAX_IMAGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            }else{
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        // 如果数据有更改，那么我们需要通知外面的监听者
        if (notifyRefresh) {
            notifySelectChanged();
        }

        return true;
    }


    public String[] getSelectedPath(){
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image: mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * clear
     */
    public void clear(){
        for(Image image:mSelectedImages) {
            /**
             * IMPORTANT TO set to false
             */
            image.isSelect = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 通知
     */
    private void notifySelectChanged(){
        SelectedChangeListener listener = mSelectedListener;
        if (listener != null) {
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }


    // notify the adapter for new data
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }

    /**
     * 用于实际的数据加载的Loader Callback
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,    //id
                MediaStore.Images.Media.DATA,   //图片路径
                MediaStore.Images.Media.DATE_ADDED  // 图片创建时间
        };

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            // create a Loader
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2]+" DESC");   // 按照时间倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            List<Image> images = new ArrayList<>();
            if (data != null) {
                int count = data.getCount();
                if (count >0) {
                    // 移动cursor到开始
                    data.moveToFirst();

                    // 得到对应列的Index坐标
                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexData = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do{
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexData);

                        File file = new File(path);
                        // 判断图片是否太小了
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE){
                            continue;
                        }

                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.data = dateTime;

                        images.add(image);
                    }while (data.moveToNext());
                }
            }

            updateSource(images);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            // 当Loader销毁或者重置了，进行界面清空
            updateSource(null);
        }
    }


    /**
     * Internal Data Structure
     */
    private static class Image{
        int id; // 数据的ID
        String path; // path of the pic
        long data;
        boolean isSelect;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }

    /**
     * 适配器
     */

    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_gallery;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

    }

    /**
     * Cell 对应的Holder
     */

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPic = itemView.findViewById(R.id.img_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelected = itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .centerCrop()
                    .placeholder(R.color.grey_200)
                    .into(mPic);
            mShade.setVisibility(image.isSelect? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    /**
     * 对外的 Listener
     */
    public interface SelectedChangeListener {
        void onSelectedCountChanged(int count);

    }
}

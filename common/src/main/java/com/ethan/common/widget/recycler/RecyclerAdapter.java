package com.ethan.common.widget.recycler;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ethan.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Ethan
 * @version 1.0.0
 *
 * @param <Data> the type of data in the list
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
    implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data>
{

    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    /**
     * Constructors
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> mListener) {
        this(new ArrayList<Data>(),mListener);
    }

    public RecyclerAdapter(List<Data> mDataList, AdapterListener<Data> mListener) {
        this.mDataList = mDataList;
        this.mListener = mListener;
    }


    /**
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * get the type of the layout
     * @param position
     * @param data
     * @return a xml id, used for creating a viewholder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * Create a ViewHolder
     * @param parent RecyclerView
     * @param viewType 界面的类型 --> viewType here == layout id
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml layout --> view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType,parent,false);

        // create a viewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root,viewType);

        // set the holder as the tag of the view
        root.setTag(R.id.tag_recycler_holder,holder);

        // set the click events
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // BindView
        holder.unbinder = ButterKnife.bind(holder,root);

        holder.callback = this;
        return holder;

    }

    /**
     * get a new view holder
     * @param root
     * @param viewType
     * @return
     */

    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        /**
         * get the data that is needed to display
         */
        Data mData = mDataList.get(position);

        /**
         * bind the data to holder
         */
        holder.bind(mData);
    }


    public abstract static class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;
        protected Data mData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Data data){
            this.mData = data;
            onBind(data);
        }

        /**
         * callback of the onbind
         * @param data
         */
        protected abstract void onBind(Data data);

        /**
         * holder to update data
         * @param data
         */
        public void updateData(Data data){
            if (this.callback != null) {
                this.callback.update(data,this);
            }
        }

    }



    // ---- data list operations ---- //
    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    /**
     * Add data and notify to change
     * @param data
     */
    public void add(Data data){
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void add(Data... dataList){
        if (dataList != null && dataList.length > 0){
            Collections.addAll(mDataList, dataList);
            int startIndex = mDataList.size();
            notifyItemRangeInserted(startIndex,dataList.length);
        }
    }

    public void add(Collection<Data> dataList){
        if (dataList != null && dataList.size() > 0){
            mDataList.addAll(dataList);
            int startIndex = mDataList.size();
            notifyItemRangeInserted(startIndex,dataList.size());
        }
    }

    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();

    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        // 得到当前viewholder的坐标
        int pos = holder.getAdapterPosition();

        // 如果找到该holder的position
        if (pos >= 0) {
            // remove and update the data
            mDataList.remove(pos);
            mDataList.add(pos,data);
            // notify the change
            notifyItemChanged(pos);
        }
    }

    private static class Image{
        int id; // 数据的ID
        String path; // 图片的路径
        long data;  // 图片的创建日期
        boolean isSelect;   // 是否选中

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

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);

        if (this.mListener != null) {
            // get the pos of the current adapter
            int pos = holder.getAdapterPosition();
            // callback
            this.mListener.onItemClick(holder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // get the pos of the current adapter
            int pos = holder.getAdapterPosition();
            // callback
            this.mListener.onItemLongClick(holder, mDataList.get(pos));
        }
        return true;
    }

    public void setListener(AdapterListener<Data> mListener) {
        this.mListener = mListener;
    }

    /**
     * customerized listener
     * @param <Data>
     */
    public interface AdapterListener<Data> {
        // listen when the cell is clicked
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);
        // listen when the cell is long clicked
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * Implement the listener
     * 对回调接口做一次实现AdapterListener
     *
     *
     * @param <Data>
     */
    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data>{

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }
}
package com.ethan.common.widget.recycler;

import androidx.recyclerview.widget.RecyclerView;

public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}

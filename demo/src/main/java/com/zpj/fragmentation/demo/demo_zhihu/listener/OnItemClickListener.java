package com.zpj.fragmentation.demo.demo_zhihu.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface OnItemClickListener {
    void onItemClick(int position, View view, RecyclerView.ViewHolder vh);
}
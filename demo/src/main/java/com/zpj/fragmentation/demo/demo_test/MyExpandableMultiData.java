package com.zpj.fragmentation.demo.demo_test;

import android.widget.ImageView;

import com.zpj.fragmentation.demo.R;
import com.zpj.recyclerview.EasyViewHolder;
import com.zpj.recyclerview.ExpandableMultiData;

import java.util.List;

public class MyExpandableMultiData extends ExpandableMultiData<Integer> {

    public MyExpandableMultiData(int pos) {
        super();
        this.list.add(pos);
        setExpand(false);
        this.hasMore = false;
    }

    @Override
    public int getHeaderLayoutId() {
        return R.layout.item_app_url_header;
    }

    @Override
    public int getChildViewType(int position) {
        return R.layout.item_app_url;
    }

    @Override
    public int getChildLayoutId(int viewType) {
        return R.layout.item_app_url;
    }

    @Override
    public void onBindHeader(EasyViewHolder holder, List<Object> payloads) {
        holder.setText(R.id.tv_title, "Header-" + this.list.get(0));
        updateIcon(holder, false);
        holder.setOnItemClickListener(v -> {
            if (isExpand()) {
                collapse();
            } else {
                expand();
            }
            updateIcon(holder, true);
        });
    }

    private void updateIcon(EasyViewHolder holder, boolean anim) {
        ImageView ivArrow = holder.getView(R.id.iv_arrow);
        if (anim) {
            float rotation;
            if (isExpand()) {
                ivArrow.setRotation(180);
                rotation = 90;
            } else {
                ivArrow.setRotation(90);
                rotation = 180;
            }
            ivArrow.animate()
                    .rotation(rotation)
                    .setDuration(360)
                    .start();
        } else {
            ivArrow.setRotation(isExpand() ? 90 : 180);
        }
    }

    @Override
    public void onBindChild(EasyViewHolder holder, List<Integer> list, int position, List<Object> payloads) {
        holder.setText(R.id.tv_content, "文件Md5:1111111111111111111111\nposition=" + list.get(position));
    }

    @Override
    public boolean loadData() {
        return false;
    }

}

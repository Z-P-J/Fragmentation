package com.zpj.fragmentation;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zpj.fragmentation.swipeback.SwipeBackFragment;
import com.zpj.fragmentation.swipeback.SwipeBackLayout;

public abstract class SimpleFragment extends SwipeBackFragment {

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (context == null) {
            context = getContext();
        }
        if (getLayoutId() > 0) {
            view = inflater.inflate(getLayoutId(), container, false);
            initView(view, savedInstanceState);
        } else {
            view = super.onCreateView(inflater, container, savedInstanceState);
        }
        if (view != null && supportSwipeBack()) {
            SwipeBackLayout.EdgeLevel level = getEdgeLevel();
            setEdgeLevel(level == null ? SwipeBackLayout.EdgeLevel.MAX : level);
            return attachToSwipeBack(view);
        } else {
            return view;
        }
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected boolean supportSwipeBack() {
        return false;
    }

    public SwipeBackLayout.EdgeLevel getEdgeLevel() {
        return SwipeBackLayout.EdgeLevel.MAX;
    }

    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

}

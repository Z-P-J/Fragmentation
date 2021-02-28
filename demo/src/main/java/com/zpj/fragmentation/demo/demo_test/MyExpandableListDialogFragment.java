package com.zpj.fragmentation.demo.demo_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.animator.ScaleAlphaAnimator;
import com.zpj.fragmentation.dialog.enums.PopupAnimation;
import com.zpj.fragmentation.dialog.impl.ListDialogFragment;
import com.zpj.recyclerview.MultiData;
import com.zpj.recyclerview.MultiRecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

public class MyExpandableListDialogFragment extends ListDialogFragment<MultiData<?>> {

    private View anchorView;

    public MyExpandableListDialogFragment() {
        setTitle("Title");
        List<MultiData<?>> multiDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            MyExpandableMultiData data = new MyExpandableMultiData(i);
            if (i == 0) {
                data.setExpand(true);
            }
            multiDataList.add(data);
        }
        setData(multiDataList);
    }

    @Override
    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
        if (anchorView == null) {
            return new ScaleAlphaAnimator(contentView, PopupAnimation.ScaleAlphaFromRightBottom);
        }
        int[] contentLocation = new int[2];
        contentView.getLocationInWindow(contentLocation);

        int[] anchorLocation = new int[2];
        anchorView.getLocationInWindow(anchorLocation);

        float pivotX = anchorLocation[0] + anchorView.getMeasuredWidth() / 2f - contentLocation[0];
        float pivotY = anchorLocation[1] + anchorView.getMeasuredHeight() / 2f - contentLocation[1];
        return new ScaleAlphaAnimator(contentView, pivotX, pivotY);

    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        tvTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void initRecyclerView(RecyclerView recyclerView, List<MultiData<?>> list) {
        MultiRecyclerViewWrapper.with(recyclerView)
                .setData(list)
                .build();
    }

    public MyExpandableListDialogFragment setAnchorView(View anchorView) {
        this.anchorView = anchorView;
        return this;
    }

}

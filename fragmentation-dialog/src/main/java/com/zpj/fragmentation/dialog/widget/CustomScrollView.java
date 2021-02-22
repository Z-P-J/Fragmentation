package com.zpj.fragmentation.dialog.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    //回调监听接口
    private OnScrollChangeListener mOnScrollChangeListener;
    //标识是否滑动到顶部
    private boolean isScrollToStart = false;
    //标识是否滑动到底部
    private boolean isScrollToEnd = false;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangeListener != null) {

            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);

            Log.i("CustomScrollView", "scrollY:" + getScrollY());

            //滚动到顶部，ScrollView存在回弹效果效应（这里只会调用两次，如果用<=0,会多次触发）
            if (isScrollToTop()) {

                //过滤操作，优化为一次调用
                if (!isScrollToStart) {
                    isScrollToStart = true;
                    Log.e("CustomScrollView", "toStart");

                    mOnScrollChangeListener.onScrollToStart();

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScrollToStart = false;
                        }
                    }, 200);
                }
            } else {
                if (isScrollToBottom()) {
                    //滚动到底部，ScrollView存在回弹效果效应
                    //优化，只过滤第一次
                    if (!isScrollToEnd) {

                        isScrollToEnd = true;
                        Log.e("CustomScrollView", "toEnd,scrollY:" + getScrollY());
                        mOnScrollChangeListener.onScrollToEnd();
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isScrollToEnd = false;
                            }
                        }, 200);
                    }
                }
            }
        }
    }

    public boolean isScrollToTop() {
        return getScrollY() == 0;
    }

    public boolean isScrollToBottom() {
        View contentView = getChildAt(0);
        return contentView != null && contentView.getMeasuredHeight() == (getScrollY() + getHeight());
    }



    //滑动监听接口

    public interface OnScrollChangeListener {

        //滑动到顶部时的回调
        void onScrollToStart();

        //滑动到底部时的回调
        void onScrollToEnd();

        void onScrollChanged(int l, int t, int oldl, int oldt);

    }



    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

}

package com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.fourth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.demo.demo_zhihu.base.BaseMainFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.fourth.child.AvatarFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.fourth.child.MeFragment;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuFourthFragment extends BaseMainFragment {

    private Toolbar mToolbar;

    public static ZhihuFourthFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuFourthFragment fragment = new ZhihuFourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_fourth;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (findChildFragment(AvatarFragment.class) == null) {
            loadFragment();
        }

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.me);
    }

    private void loadFragment() {
        loadRootFragment(R.id.fl_fourth_container_upper, AvatarFragment.newInstance());
        loadRootFragment(R.id.fl_fourth_container_lower, MeFragment.newInstance());
    }

    public void onBackToFirstFragment() {
        _mBackToFirstListener.onBackToFirstFragment();
    }
}

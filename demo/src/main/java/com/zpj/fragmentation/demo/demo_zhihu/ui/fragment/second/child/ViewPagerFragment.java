package com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.second.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zpj.fragmentation.SimpleFragment;
import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.demo.demo_zhihu.adapter.ZhihuPagerFragmentAdapter;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class ViewPagerFragment extends SimpleFragment {
    private TabLayout mTab;
    private ViewPager mViewPager;

    public static ViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_second_pager;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        mTab = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewPager);

        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

        mViewPager.setAdapter(new ZhihuPagerFragmentAdapter(getChildFragmentManager(),
                getString(R.string.recommend), getString(R.string.hot), getString(R.string.favorite),
                getString(R.string.more)));
        mTab.setupWithViewPager(mViewPager);
    }

}

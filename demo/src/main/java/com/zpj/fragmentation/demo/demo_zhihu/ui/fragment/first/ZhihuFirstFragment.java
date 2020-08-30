package com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.demo.demo_zhihu.base.BaseMainFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.first.child.FirstHomeFragment;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class ZhihuFirstFragment extends BaseMainFragment {

    public static ZhihuFirstFragment newInstance() {

        Bundle args = new Bundle();

        ZhihuFirstFragment fragment = new ZhihuFirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_first;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        if (findChildFragment(FirstHomeFragment.class) == null) {
            loadRootFragment(R.id.fl_first_container, FirstHomeFragment.newInstance());
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        for (int i = 0; i < 5; i++) {
            final int index = i;
            postOnSupportVisible(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "ZhihuFirstFragment visible" + index, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

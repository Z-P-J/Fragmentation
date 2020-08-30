package com.zpj.fragmentation.demo.demo_zhihu.base;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zpj.fragmentation.BaseFragment;
import com.zpj.fragmentation.SupportFragment;
import com.zpj.fragmentation.demo.R;

/**
 * Created by YoKeyword on 16/2/7.
 */
public abstract class BaseBackFragment extends BaseFragment {

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }
}

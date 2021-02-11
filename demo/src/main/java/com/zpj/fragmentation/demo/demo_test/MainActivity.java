package com.zpj.fragmentation.demo.demo_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.zpj.fragmentation.SupportActivity;
import com.zpj.fragmentation.SupportFragment;
import com.zpj.fragmentation.anim.DefaultHorizontalAnimator;
import com.zpj.fragmentation.anim.DefaultNoAnimator;
import com.zpj.fragmentation.anim.DefaultVerticalAnimator;
import com.zpj.fragmentation.anim.FragmentAnimator;
import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.demo.demo_zhihu.base.BaseMainFragment;
import com.zpj.fragmentation.demo.demo_zhihu.event.TabSelectedEvent;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.first.ZhihuFirstFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.first.child.FirstHomeFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.fourth.ZhihuFourthFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.fourth.child.MeFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.second.ZhihuSecondFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.second.child.ViewPagerFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.third.ZhihuThirdFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.third.child.ShopFragment;
import com.zpj.fragmentation.demo.demo_zhihu.ui.view.BottomBar;
import com.zpj.fragmentation.demo.demo_zhihu.ui.view.BottomBarTab;
import com.zpj.fragmentation.dialog.AbstractDialogFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * 类知乎 复杂嵌套Demo tip: 多使用右上角的"查看栈视图"
 * Created by YoKeyword on 16/6/2.
 */
public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_main);
        MainFragment fragment = findFragment(MainFragment.class);
        if (fragment == null) {
            fragment = new MainFragment();
            loadRootFragment(R.id.fl_container, fragment);
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
//        return new DefaultNoAnimator();
    }
}

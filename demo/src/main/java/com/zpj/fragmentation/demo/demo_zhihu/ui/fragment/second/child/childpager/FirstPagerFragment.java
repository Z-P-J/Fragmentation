package com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.second.child.childpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.zpj.fragmentation.SimpleFragment;
import com.zpj.fragmentation.SupportFragment;
import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.demo.demo_zhihu.MainActivity;
import com.zpj.fragmentation.demo.demo_zhihu.adapter.HomeAdapter;
import com.zpj.fragmentation.demo.demo_zhihu.entity.Article;
import com.zpj.fragmentation.demo.demo_zhihu.event.TabSelectedEvent;
import com.zpj.fragmentation.demo.demo_zhihu.listener.OnItemClickListener;
import com.zpj.fragmentation.demo.demo_zhihu.ui.fragment.second.child.DetailFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoKeyword on 16/6/3.
 */
public class FirstPagerFragment extends SimpleFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecy;
    private SwipeRefreshLayout mRefreshLayout;
    private HomeAdapter mAdapter;
    private boolean mAtTop = true;
    private int mScrollTotal;
    private String[] mTitles;
    private String[] mContents;

    public static FirstPagerFragment newInstance() {

        Bundle args = new Bundle();

        FirstPagerFragment fragment = new FirstPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zhihu_fragment_second_pager_first;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mRecy = findViewById(R.id.recy);
        mRefreshLayout = findViewById(R.id.refresh_layout);

        mTitles = getResources().getStringArray(R.array.array_title);
        mContents = getResources().getStringArray(R.array.array_content);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new HomeAdapter(_mActivity);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        mRecy.setLayoutManager(manager);
        mRecy.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                // 这里的DetailFragment在flow包里
                // 这里是父Fragment启动,要注意 栈层级
                ((SupportFragment) getParentFragment()).start(DetailFragment.newInstance(mAdapter.getItem(position).getTitle()));
            }
        });

        // Init Datas
        List<Article> articleList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int index = (int) (Math.random() * 3);
            Article article = new Article(mTitles[index], mContents[index]);
            articleList.add(article);
        }
        mAdapter.setDatas(articleList);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                mAtTop = mScrollTotal <= 0;
            }
        });

        postOnEnterAnimationEnd(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "OnEnterAnimationEnd", Toast.LENGTH_SHORT).show();
            }
        });

        postOnEnterAnimationEndDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "OnEnterAnimationEndDelayed-----5000", Toast.LENGTH_SHORT).show();
            }
        }, 5000);

        postOnSupportVisible(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "OnSupportVisible", Toast.LENGTH_SHORT).show();
            }
        });

        postOnSupportVisibleDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "OnSupportVisibleDelayed-----10000", Toast.LENGTH_SHORT).show();
            }
        }, 10000);

    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
    }

    /**
     * 选择tab事件
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainActivity.SECOND) return;

        if (mAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        } else {
            scrollToTop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}

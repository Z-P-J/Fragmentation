package com.zpj.fragmentation.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zpj.fragmentation.SupportFragment;
import com.zpj.fragmentation.anim.DefaultNoAnimator;
import com.zpj.fragmentation.anim.FragmentAnimator;

public abstract class AbstractDialogFragment extends SupportFragment {

    protected long showAnimDuration = 360;
    protected long dismissAnimDuration = 360;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    protected abstract boolean onBackPressed();

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId() > 0) {
            view = inflater.inflate(getLayoutId(), container, false);
        } else {
            view = super.onCreateView(inflater, container, savedInstanceState);
        }
        initView(view, savedInstanceState);
        return view;
    }

    @Deprecated
    @Override
    public final void setFragmentAnimator(FragmentAnimator fragmentAnimator) {

    }

    @Override
    public final FragmentAnimator getFragmentAnimator() {
        return onCreateFragmentAnimator();
    }

    @Override
    public final FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }

    @Override
    public final void onEnterAnimationEnd(Bundle savedInstanceState) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onShowAnimationEnd(savedInstanceState);
            }
        }, getShowAnimDuration());
    }

    public void onShowAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
    }

//    @Override
//    public void onSupportVisible() {
//        super.onSupportVisible();
//    }
//
//    @Override
//    public void onSupportInvisible() {
//        super.onSupportInvisible();
//    }

    @Override
    public final boolean onBackPressedSupport() {
        if (onBackPressed()) {
            return true;
        }
        dismiss();
        return true;
    }

    public long getShowAnimDuration() {
        if (showAnimDuration < 0) {
            return 0;
        }
        return showAnimDuration;
    }

    public long getDismissAnimDuration() {
        if (dismissAnimDuration < 0) {
            return 0;
        }
        return dismissAnimDuration;
    }

    public abstract void dismiss();

}

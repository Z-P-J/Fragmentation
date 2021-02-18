package com.zpj.fragmentation.dialog.impl;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.animator.TranslateAnimator;
import com.zpj.fragmentation.dialog.base.BaseDialogFragment;
import com.zpj.fragmentation.dialog.enums.PopupAnimation;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class FullScreenDialogFragment extends BaseDialogFragment {

//    protected abstract int getContentLayoutId();

    @Override
    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
        return new TranslateAnimator(contentView, PopupAnimation.TranslateFromBottom);
    }

    @Override
    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
        return null;
    }

//    @Override
//    protected void initView(View view, @Nullable Bundle savedInstanceState) {
//        super.initView(view, savedInstanceState);
//    }

    @Override
    protected void initLayoutParams(ViewGroup view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = MATCH_PARENT;
        layoutParams.width = MATCH_PARENT;
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setClickable(true);
    }

//    @Override
//    public int getMarginBottom() {
//        return 0;
//    }
//
//    @Override
//    public int getMarginEnd() {
//        return 0;
//    }

}

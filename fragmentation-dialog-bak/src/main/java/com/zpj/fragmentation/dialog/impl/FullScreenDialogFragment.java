package com.zpj.fragmentation.dialog.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.fragmentation.dialog.base.CenterDialogFragment;
import com.zpj.popup.animator.PopupAnimator;
import com.zpj.popup.animator.TranslateAnimator;
import com.zpj.popup.enums.PopupAnimation;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Description: 宽高撑满的全屏弹窗
 * Create by lxj, at 2019/2/1
 */
public abstract class FullScreenDialogFragment extends CenterDialogFragment {

    @Override
    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
        return new TranslateAnimator(contentView, PopupAnimation.TranslateFromBottom);
    }

    @Override
    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
        return null;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getImplView().getLayoutParams();
        layoutParams.height = MATCH_PARENT;
        layoutParams.width = MATCH_PARENT;

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getContentView().getLayoutParams();
        params.height = MATCH_PARENT;
        params.width = MATCH_PARENT;
        params.gravity = Gravity.CENTER;

    }
}

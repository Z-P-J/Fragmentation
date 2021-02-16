package com.zpj.fragmentation.dialog.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lihang.ShadowLayout;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.animator.ScaleAlphaAnimator;
import com.zpj.fragmentation.dialog.enums.PopupAnimation;
import com.zpj.fragmentation.dialog.impl.FullScreenDialogFragment;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.utils.ScreenUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class CenterDialogFragment extends BaseDialogFragment {

    protected View contentView;

    public CenterDialogFragment() {
        setMaxWidth(MATCH_PARENT);
        int dp16 = ScreenUtils.dp2pxInt(16);
        setMarginHorizontal(dp16);
        setMarginVertical(dp16 * 2);
    }

    @Override
    protected final int getImplLayoutId() {
        return R.layout._dialog_layout_center_view;
    }

    protected abstract int getContentLayoutId();

    @Override
    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
        return new ScaleAlphaAnimator(contentView, PopupAnimation.ScaleAlphaFromCenter);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        ShadowLayout centerPopupContainer = findViewById(R.id.centerPopupContainer);
        if (getContentLayoutId() > 0) {
            contentView = getLayoutInflater().inflate(getContentLayoutId(), null, false);
            centerPopupContainer.addView(contentView);
            if (contentView != null) {
                if (bgDrawable != null) {
                    contentView.setBackground(bgDrawable);
                } else {
                    contentView.setBackground(DialogThemeUtils.getCenterDialogBackground(context));
                }
            }
        }


//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)  centerPopupContainer.getLayoutParams();

//        float dp16 = ScreenUtils.dp2px(context, 16);
//        int maxHeight = getMaxHeight();
//        if (maxHeight == WRAP_CONTENT || maxHeight == MATCH_PARENT) {
//            int screenHeight = ScreenUtils.getScreenHeight(context);
//            int screenWidth = ScreenUtils.getScreenWidth(context);
////                int margin = (int) (screenHeight * 0.07f - dp16);
//            int margin = (int) (screenHeight * dp16 * 2 / screenWidth - dp16);
//            params.topMargin = margin;
//            params.bottomMargin = margin;
//        }
//        params.height = maxHeight;
//
//        int maxWidth = getMaxWidth();
//        if (maxWidth == WRAP_CONTENT || maxWidth == MATCH_PARENT) {
////                int screenWidth = ScreenUtils.getScreenWidth(context);
////                int margin = (int) (screenWidth * 0.07f - dp16);
//            int margin = (int) (dp16);
//            params.leftMargin = margin;
//            params.rightMargin = margin;
//        }
//        params.width = maxWidth;
//
//        params.gravity = getGravity(); // Gravity.CENTER

    }

    public View getContentView() {
        return contentView;
    }

//    protected int getMaxWidth() {
//        return MATCH_PARENT;
//    }
//
//    protected int getMaxHeight() {
//        return WRAP_CONTENT;
//    }

}

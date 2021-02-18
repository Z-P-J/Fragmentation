//package com.zpj.fragmentation.dialog.base;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import com.lihang.ShadowLayout;
//import com.zpj.fragmentation.dialog.R;
//import com.zpj.fragmentation.dialog.animator.PopupAnimator;
//import com.zpj.fragmentation.dialog.animator.ScaleAlphaAnimator;
//import com.zpj.fragmentation.dialog.enums.PopupAnimation;
//import com.zpj.fragmentation.dialog.impl.FullScreenDialogFragment;
//import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
//import com.zpj.utils.ScreenUtils;
//
//import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
//import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//
//public abstract class CardDialogFragment_old extends BaseDialogFragment {
//
//    protected View contentView;
//
//    public CardDialogFragment_old() {
//        setMaxWidth(MATCH_PARENT);
//        int dp16 = ScreenUtils.dp2pxInt(16);
//        setMarginHorizontal(dp16);
//        setMarginVertical(dp16 * 2);
//    }
//
//    @Override
//    protected int getImplLayoutId() {
//        return R.layout._dialog_layout_center_view;
//    }
//
//    protected abstract int getContentLayoutId();
//
//    @Override
//    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
//        return new ScaleAlphaAnimator(contentView, PopupAnimation.ScaleAlphaFromCenter);
//    }
//
//    @Override
//    protected void initView(View view, @Nullable Bundle savedInstanceState) {
//        super.initView(view, savedInstanceState);
//
//        ViewGroup centerPopupContainer = getImplView();
//        if (getContentLayoutId() > 0) {
//            contentView = getLayoutInflater().inflate(getContentLayoutId(), null, false);
//            centerPopupContainer.addView(contentView);
//            if (bgDrawable != null) {
//                contentView.setBackground(bgDrawable);
//            } else {
//                contentView.setBackground(DialogThemeUtils.getCenterDialogBackground(context));
//            }
//        }
//    }
//
//    public View getContentView() {
//        return contentView;
//    }
//
//}

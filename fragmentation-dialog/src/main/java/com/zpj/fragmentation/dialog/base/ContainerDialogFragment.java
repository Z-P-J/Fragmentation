package com.zpj.fragmentation.dialog.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lihang.ShadowLayout;
import com.zpj.fragmentation.anim.DefaultNoAnimator;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.animator.ScaleAlphaAnimator;
import com.zpj.fragmentation.dialog.enums.PopupAnimation;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.fragmentation.dialog.widget.SmartDragLayout;
import com.zpj.utils.ScreenUtils;

import java.lang.reflect.Field;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class ContainerDialogFragment extends BaseDialogFragment {

    protected ViewGroup contentView;

    public ContainerDialogFragment() {
        setMaxWidth(MATCH_PARENT);
        if (!isDragDialog()) {
            int dp16 = ScreenUtils.dp2pxInt(16);
            setMarginHorizontal(dp16);
            setMarginVertical(dp16 * 2);
        }
    }

    protected abstract boolean isDragDialog();

    @Override
    protected int getImplLayoutId() {
        if (isDragDialog()) {
            return R.layout._dialog_layout_bottom_view;
        }
        return R.layout._dialog_layout_center_view;
    }

    @Override
    protected int getGravity() {
        if (isDragDialog()) {
            return Gravity.BOTTOM;
        }
        return super.getGravity();
    }

    protected abstract int getContentLayoutId();

    @Override
    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
        if (isDragDialog()) {
            return null;
        }
        return new ScaleAlphaAnimator(contentView, PopupAnimation.ScaleAlphaFromCenter);
    }

    @Override
    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
        if (isDragDialog()) {
            return null;
        }
        return super.getShadowAnimator(flContainer);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        if (isDragDialog()) {
            SmartDragLayout bottomPopupContainer = (SmartDragLayout) getImplView();
            contentView = (ViewGroup) getLayoutInflater().inflate(getContentLayoutId(), null, false);
//            if (getMarginTop() > 0 || getMarginBottom() > 0) {
//                CardView flContainer = new CardView(context);
//                flContainer.setCardBackgroundColor(DialogThemeUtils.getDialogBackgroundColor(context));
//                flContainer.addView(contentView);
//                bottomPopupContainer.addView(flContainer);
//            } else {
//                bottomPopupContainer.addView(contentView);
//            }

            CardView cardView = new CardView(context);
            cardView.setCardBackgroundColor(Color.TRANSPARENT);
            int dp8 = ScreenUtils.dp2pxInt(8);
            cardView.setRadius(dp8);
            cardView.setUseCompatPadding(false);
            cardView.setCardElevation(0);
            cardView.addView(contentView);
            ShadowLayout shadowLayout = new ShadowLayout(context);
            try {
                Field mBackGroundColor = ShadowLayout.class.getDeclaredField("mBackGroundColor");
                mBackGroundColor.setAccessible(true);
                mBackGroundColor.set(shadowLayout, DialogThemeUtils.getDialogBackgroundColor(context));
                Field mCornerRadiusLeftTop = ShadowLayout.class.getDeclaredField("mCornerRadius_leftTop");
                Field mCornerRadiusRightTop = ShadowLayout.class.getDeclaredField("mCornerRadius_rightTop");
                mCornerRadiusLeftTop.setAccessible(true);
                mCornerRadiusRightTop.setAccessible(true);
                mCornerRadiusLeftTop.set(shadowLayout, dp8);
                mCornerRadiusRightTop.set(shadowLayout, dp8);
                shadowLayout.setSelected(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            shadowLayout.setmShadowLimit(0);
            shadowLayout.addView(cardView);

            contentView = shadowLayout;
            if (getMarginTop() > 0 || getMarginBottom() > 0) {
                FrameLayout flContainer = new FrameLayout(context);
                flContainer.addView(contentView);
                bottomPopupContainer.addView(flContainer);
            } else {
                bottomPopupContainer.addView(contentView);
            }

//            bottomPopupContainer.addView(shadowLayout, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

            if (bgDrawable != null) {
                contentView.setBackground(bgDrawable);
            } else {
//                contentView.setBackground(DialogThemeUtils.getBottomDialogBackground(context));
            }

            super.initLayoutParams(contentView);
            contentView.setClickable(false);
            bottomPopupContainer.bindContentView(contentView);

            bottomPopupContainer.enableDrag(true);
            bottomPopupContainer.dismissOnTouchOutside(true);
            bottomPopupContainer.handleTouchOutsideEvent(true);
            bottomPopupContainer.hasShadowBg(true);

            bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
                @Override
                public void onClose() {
                    setFragmentAnimator(new DefaultNoAnimator());
                    postOnEnterAnimationEnd(() -> {
                        ContainerDialogFragment.super.doDismissAnimation();
                        popThis();
                        onDismiss();
                    });
                }
                @Override
                public void onOpen() {

                }
            });

            bottomPopupContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            ShadowLayout shadowLayout = findViewById(R.id.centerPopupContainer);
            try {
                int color = DialogThemeUtils.getDialogBackgroundColor(context);
                Field mBackGroundColor = ShadowLayout.class.getDeclaredField("mBackGroundColor");
                mBackGroundColor.setAccessible(true);
                mBackGroundColor.set(shadowLayout, color);
                shadowLayout.setSelected(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ViewGroup centerPopupContainer = findViewById(R.id._dialog_card_view);
            if (getContentLayoutId() > 0) {
                contentView = (ViewGroup) getLayoutInflater().inflate(getContentLayoutId(), null, false);
                centerPopupContainer.addView(contentView);
                if (bgDrawable != null) {
                    contentView.setBackground(bgDrawable);
                } else {
//                    contentView.setBackground(DialogThemeUtils.getCenterDialogBackground(context));
                }
            }
        }
    }

    @Override
    protected void initLayoutParams(ViewGroup view) {
        if (!isDragDialog()) {
            super.initLayoutParams(view);
        }
    }

    @Override
    public void doShowAnimation() {
        super.doShowAnimation();
        if (isDragDialog() && getImplView() instanceof SmartDragLayout) {
            ((SmartDragLayout) getImplView()).open();
        }
    }

    @Override
    public void doDismissAnimation() {
        super.doDismissAnimation();
        if (isDragDialog() && getImplView() instanceof SmartDragLayout) {
            ((SmartDragLayout) getImplView()).close();
        }
    }

}

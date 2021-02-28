package com.zpj.fragmentation.dialog.base;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.fragmentation.anim.DefaultNoAnimator;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.animator.ScaleAlphaAnimator;
import com.zpj.fragmentation.dialog.enums.PopupAnimation;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.fragmentation.dialog.widget.SmartDragLayout;
import com.zpj.utils.ScreenUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class ContainerDialogFragment extends BaseDialogFragment {

    protected ViewGroup contentView;

    public ContainerDialogFragment() {
        setMaxWidth(MATCH_PARENT);
        if (!isDragDialog()) {
            setMarginHorizontal((int) (ScreenUtils.getScreenWidth() * 0.08f));
            setMarginVertical((int) (ScreenUtils.getScreenHeight() * 0.08f));
        }
    }

    protected abstract boolean isDragDialog();

    @Override
    protected final int getImplLayoutId() {
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
            if (getMarginTop() > 0 || getMarginBottom() > 0) {
                FrameLayout flContainer = new FrameLayout(context);
                flContainer.addView(contentView);
                bottomPopupContainer.addView(flContainer);
            } else {
                bottomPopupContainer.addView(contentView);
            }

            if (bgDrawable != null) {
                contentView.setBackground(bgDrawable);
            } else {
//                contentView.setBackground(builder.setBgColor(DialogThemeUtils.getDialogBackgroundColor(context)).builder());
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(DialogThemeUtils.getDialogBackgroundColor(context));
                drawable.setShape(GradientDrawable.RECTANGLE);
                int size = ScreenUtils.dp2pxInt(8);
                drawable.setCornerRadii(new float[]{ size, size, size, size, 0, 0, 0, 0 });
                contentView.setBackground(drawable);
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
//            ViewGroup centerPopupContainer = findViewById(R.id._dialog_card_view);
            CardView cardView = findViewById(R.id.centerPopupContainer);
            if (getContentLayoutId() > 0) {
                contentView = (ViewGroup) getLayoutInflater().inflate(getContentLayoutId(), cardView, false);

                cardView.setUseCompatPadding(false);
                if (bgDrawable != null) {
                    contentView.setBackground(bgDrawable);
                    cardView.setCardElevation(0);
                    cardView.setRadius(0);
                    cardView.setCardBackgroundColor(Color.TRANSPARENT);
                } else {
//                    contentView.setBackground(DialogThemeUtils.getCenterDialogBackground(context));
                    int dp8 = ScreenUtils.dp2pxInt(8);
                    cardView.setCardElevation(dp8 / 2f);
                    cardView.setRadius(dp8);
                    cardView.setCardBackgroundColor(DialogThemeUtils.getDialogBackgroundColor(context));
                }
                cardView.addView(contentView);
                super.initLayoutParams(cardView);
            }
        }
    }

    @Override
    protected void initLayoutParams(ViewGroup view) {
//        if (!isDragDialog()) {
//            super.initLayoutParams(view);
//        }
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

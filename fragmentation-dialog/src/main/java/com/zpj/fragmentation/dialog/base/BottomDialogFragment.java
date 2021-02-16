package com.zpj.fragmentation.dialog.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.fragmentation.anim.DefaultNoAnimator;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.fragmentation.dialog.widget.SmartDragLayout;
import com.zpj.utils.ScreenUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BottomDialogFragment extends BaseDialogFragment {

    protected SmartDragLayout bottomPopupContainer;

    private ViewGroup contentView;

    protected Boolean enableDrag = true;

    public BottomDialogFragment() {
        setMaxWidth(MATCH_PARENT);
    }

    @Override
    protected final int getImplLayoutId() {
        return R.layout._dialog_layout_bottom_view;
    }

    protected abstract int getContentLayoutId();

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
        return null;
    }

    @Override
    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
        return null;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        bottomPopupContainer = findViewById(R.id.bottomPopupContainer);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) bottomPopupContainer.getLayoutParams();
        layoutParams.height = MATCH_PARENT;
        layoutParams.width = MATCH_PARENT;


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
            contentView.setBackground(DialogThemeUtils.getBottomDialogBackground(context));
        }

        super.initLayoutParams(contentView);
        contentView.setClickable(false);
        bottomPopupContainer.bindContentView(contentView);
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) contentView.getLayoutParams();
//        int maxHeight = getMaxHeight();
//        if (maxHeight > 0) {
//            params.height = WRAP_CONTENT;
//            int topMargin = ScreenUtils.getScreenHeight(context) - maxHeight;
//            if (topMargin < 0) {
//                topMargin = 0;
//            }
//            params.topMargin = topMargin;
//        } else {
//            params.height = maxHeight;
//        }
////        params.height = getMaxHeight();
//        params.width = getMaxWidth();
//        params.gravity = getGravity();

        bottomPopupContainer.enableDrag(enableDrag);
        bottomPopupContainer.dismissOnTouchOutside(true);
        bottomPopupContainer.handleTouchOutsideEvent(true);
        bottomPopupContainer.hasShadowBg(true);

        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                setFragmentAnimator(new DefaultNoAnimator());
                postOnEnterAnimationEnd(() -> {
                    BottomDialogFragment.super.doDismissAnimation();
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

//        Utility.applyPopupSize(getImplView(), getMaxWidth(), 0);

    }

    @Override
    protected void initLayoutParams(ViewGroup view) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void doShowAnimation() {
        super.doShowAnimation();
        bottomPopupContainer.open();
    }

    @Override
    public void doDismissAnimation() {
        super.doDismissAnimation();
        bottomPopupContainer.close();
    }

    public View getContentView() {
        return contentView;
    }
}

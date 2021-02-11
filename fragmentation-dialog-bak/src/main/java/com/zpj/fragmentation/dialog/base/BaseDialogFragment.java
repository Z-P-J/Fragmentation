package com.zpj.fragmentation.dialog.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.fragmentation.SupportActivity;
import com.zpj.fragmentation.SupportFragment;
import com.zpj.fragmentation.dialog.AbstractDialogFragment;
import com.zpj.popup.R;
import com.zpj.popup.animator.PopupAnimator;
import com.zpj.popup.animator.ShadowBgAnimator;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseDialogFragment extends AbstractDialogFragment {

    protected PopupAnimator popupContentAnimator;
    protected PopupAnimator shadowBgAnimator;

    private FrameLayout rootView;
    private View implView;

    private boolean isDismissing;

    protected boolean cancelable = true;
    protected boolean cancelableInTouchOutside = true;

    @Override
    protected final int getLayoutId() {
        return R.layout._dialog_layout_dialog_view;
    }

    protected abstract int getImplLayoutId();

    protected abstract int getGravity();

    protected abstract PopupAnimator getDialogAnimator(ViewGroup contentView);

    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
        return new ShadowBgAnimator(flContainer);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        FrameLayout flContainer = findViewById(R.id._dialog_fl_container);
        this.rootView = flContainer;

        flContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cancelable || !cancelableInTouchOutside) {
                    return;
                }
                pop();
            }
        });

        flContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        implView = LayoutInflater.from(context).inflate(getImplLayoutId(), null, false);
        implView.setFocusableInTouchMode(true);
        implView.setFocusable(true);
        implView.setClickable(true);
        flContainer.addView(implView);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) implView.getLayoutParams();
        params.gravity = getGravity();
        params.height = WRAP_CONTENT;
        params.width = WRAP_CONTENT;

        shadowBgAnimator = getShadowAnimator(flContainer);
        popupContentAnimator = getDialogAnimator((ViewGroup) implView);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        doShowAnimation();

//        doAfterShow();

    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @Override
    public void pop() {
        if (!cancelable) {
            return;
        }
        dismiss();
    }

    public BaseDialogFragment show(SupportFragment fragment) {
        fragment.start(this);
        return this;
    }

    public BaseDialogFragment show(SupportActivity activity) {
        activity.start(this);
        return this;
    }

    public void doShowAnimation() {
        if (shadowBgAnimator != null) {
            shadowBgAnimator.initAnimator();
            shadowBgAnimator.animateShow();
        }

        if (popupContentAnimator != null) {
            popupContentAnimator.initAnimator();
            popupContentAnimator.animateShow();
        }
    }

    public void doDismissAnimation() {
        if (popupContentAnimator != null) {
            popupContentAnimator.animateDismiss();
        }
        if (shadowBgAnimator != null) {
            shadowBgAnimator.animateDismiss();
        }
    }

    public void onDismiss() {

    }

    public void dismiss() {
        if (!isDismissing) {
            isDismissing = true;
            doDismissAnimation();
            super.pop();
//            postDelayed(() -> {
//                BaseDialogFragment.super.popThis();
//                onDismiss();
//            }, XPopup.getAnimationDuration());
        }
    }

    protected FrameLayout getRootView() {
        return rootView;
    }

    protected View getImplView() {
        return implView;
    }


    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void setCancelableInTouchOutside(boolean cancelableInTouchOutside) {
        this.cancelableInTouchOutside = cancelableInTouchOutside;
    }
}

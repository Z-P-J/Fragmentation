package com.zpj.fragmentation.dialog.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zpj.fragmentation.ISupportFragment;
import com.zpj.fragmentation.SupportActivity;
import com.zpj.fragmentation.SupportFragment;
import com.zpj.fragmentation.dialog.AbstractDialogFragment;
import com.zpj.fragmentation.dialog.IDialog;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.animator.ShadowBgAnimator;
import com.zpj.utils.ContextUtils;
import com.zpj.utils.ScreenUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseDialogFragment<T extends BaseDialogFragment<T>> extends AbstractDialogFragment {

    protected PopupAnimator popupContentAnimator;
    protected PopupAnimator shadowBgAnimator;

    private FrameLayout rootView;
    private ViewGroup implView;

    protected boolean isDismissing;

    protected boolean interceptTouch = true;
    protected boolean cancelable = true;
    protected boolean cancelableInTouchOutside = true;

    private int gravity = Gravity.CENTER;

    protected int maxWidth = WRAP_CONTENT;
    protected int maxHeight = WRAP_CONTENT;
    protected int marginStart, marginTop, marginEnd, marginBottom;

    protected IDialog.OnDismissListener onDismissListener;

    private ISupportFragment preFragment;

    protected Drawable bgDrawable;

    @Override
    protected final int getLayoutId() {
        return R.layout._dialog_layout_dialog_view;
    }

    protected abstract int getImplLayoutId();

    protected abstract PopupAnimator getDialogAnimator(ViewGroup contentView);

    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
        return new ShadowBgAnimator(flContainer);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        preFragment = getPreFragment();
        FrameLayout flContainer = findViewById(R.id._dialog_fl_container);
        this.rootView = flContainer;

        if (interceptTouch) {
            interceptTouch();
        }


        implView = (ViewGroup) getLayoutInflater().inflate(getImplLayoutId(), flContainer, false);
        flContainer.addView(implView);

        initLayoutParams(implView);

        shadowBgAnimator = getShadowAnimator(flContainer);

    }

    protected void initLayoutParams(ViewGroup view) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = getGravity();
        params.leftMargin = getMarginStart();
        params.topMargin = getMarginTop();
        params.rightMargin = getMarginEnd();
        params.bottomMargin = getMarginBottom();
        params.height = getMaxHeight();
        params.width = getMaxWidth();
        view.setFocusableInTouchMode(true);
        view.setFocusable(true);
        view.setClickable(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRootView().getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        doShowAnimation();
//                        if (preFragment != null) {
//                            preFragment.onPause();
//                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (preFragment != null && preFragment == getTopFragment()) {
            preFragment.onSupportVisible();
        }
        preFragment = null;
        this.isDismissing = false;
        super.onDestroy();
    }

    @Override
    protected boolean onBackPressed() {
        return !cancelable;
    }

    @Override
    public void pop() {
        if (!cancelable) {
            return;
        }
        dismiss();
    }

    protected T self() {
        return (T) this;
    }

    public T show(SupportFragment fragment) {
        onBeforeShow();
        fragment.start(this);
        return self();
    }

    public T show(Context context) {
        onBeforeShow();
        Activity activity = ContextUtils.getActivity(context);
        if (activity instanceof SupportActivity) {
            ((SupportActivity) activity).start(this);
        } else if (activity instanceof FragmentActivity) {
            FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, "tag");
            ft.commit();
        } else {
            Toast.makeText(context, "启动DialogFragment失败", Toast.LENGTH_SHORT).show();
        }
        return self();
    }

    public T show(SupportActivity activity) {
        onBeforeShow();
        activity.start(this);
        return self();
    }

    public void doShowAnimation() {
        popupContentAnimator = getDialogAnimator(implView);
        if (popupContentAnimator != null) {
            popupContentAnimator.initAnimator();
            popupContentAnimator.animateShow();
        }

        if (shadowBgAnimator != null) {
            shadowBgAnimator.initAnimator();
            shadowBgAnimator.animateShow();
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

    public void dismiss() {
        postOnEnterAnimationEnd(() -> {
            if (!isDismissing) {
                isDismissing = true;
                doDismissAnimation();

                onDismiss();
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseDialogFragment.super.popThis();
                    }
                }, getDismissAnimDuration());
            }
        });
    }

    protected void onDismiss() {
//        isDismissing = false;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    protected void onBeforeShow() {
        isDismissing = false;
    }

    protected void onHide() {

    }

    protected int getGravity() {
        return gravity;
    }

    protected int getMaxWidth() {
        return maxWidth;
    }

    protected int getMaxHeight() {
        return maxHeight;
    }

    public int getMarginStart() {
        return marginStart;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public int getMarginEnd() {
        return marginEnd;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    protected FrameLayout getRootView() {
        return rootView;
    }

    protected ViewGroup getImplView() {
        return implView;
    }

    public T setShowAnimDuration(long duration) {
        this.showAnimDuration = duration;
        return self();
    }

    public T setDismissAnimDuration(long duration) {
        this.dismissAnimDuration = duration;
        return self();
    }

    public T setDialogBackground(Drawable bgDrawable) {
        this.bgDrawable = bgDrawable;
        return self();
    }

    public T setGravity(int gravity) {
        this.gravity = gravity;
        return self();
    }

    public T setMaxWidth(int maxWidth) {
        if (maxWidth == WRAP_CONTENT || maxWidth == MATCH_PARENT) {
            this.maxWidth = maxWidth;
        } else if (maxWidth >= 0) {
            this.maxWidth = maxWidth;
            int margin = ScreenUtils.getScreenWidth() - maxWidth;
            if (margin > 0) {
                setMarginStart(margin);
                setMarginEnd(margin);
            }
        }
        return self();
    }

    public T setMaxHeight(int maxHeight) {
        if (maxHeight == WRAP_CONTENT || maxHeight == MATCH_PARENT) {
            this.maxHeight = maxHeight;
        } else if (maxHeight >= 0) {
            this.maxHeight = maxHeight;
            int margin = ScreenUtils.getScreenHeight() - maxHeight;
            if (margin > 0) {
                setMarginTop(margin);
                setMarginBottom(margin);
            }
        }
        return self();
    }

    public T setMarginStart(int marginStart) {
        this.marginStart = marginStart;
        return self();
    }

    public T setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        return self();
    }

    public T setMarginEnd(int marginEnd) {
        this.marginEnd = marginEnd;
        return self();
    }

    public T setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
        return self();
    }

    public T setMarginHorizontal(int margin) {
        setMarginStart(margin);
        setMarginEnd(margin);
        return self();
    }

    public T setMarginVertical(int margin) {
        setMarginTop(margin);
        setMarginBottom(margin);
        return self();
    }

    public T setInterceptTouch(boolean interceptTouch) {
        this.interceptTouch = interceptTouch;
        if (rootView != null) {
            if (interceptTouch) {
                interceptTouch();
            } else {
                rootView.setOnClickListener(null);
                rootView.setOnLongClickListener(null);
                rootView.setClickable(false);
                rootView.setLongClickable(false);
            }
        }
        return self();
    }

    private void interceptTouch() {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cancelable || !cancelableInTouchOutside) {
                    return;
                }
                dismiss();
            }
        });

        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    public T setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return self();
    }

    public T setCancelableInTouchOutside(boolean cancelableInTouchOutside) {
        this.cancelableInTouchOutside = cancelableInTouchOutside;
        return self();
    }

    public T setOnDismissListener(IDialog.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return self();
    }

//    public BaseDialogFragment setBackgroundDrawable(Drawable drawable) {
//        this.bgDrawable = drawable;
//        return this;
//    }

}

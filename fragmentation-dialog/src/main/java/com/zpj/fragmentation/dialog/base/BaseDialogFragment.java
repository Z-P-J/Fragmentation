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

public abstract class BaseDialogFragment extends AbstractDialogFragment {

    protected PopupAnimator popupContentAnimator;
    protected PopupAnimator shadowBgAnimator;

    private FrameLayout rootView;
    private ViewGroup implView;

    private boolean isDismissing;

    protected boolean cancelable = true;
    protected boolean cancelableInTouchOutside = true;

    private int gravity = Gravity.CENTER;

    private int maxWidth = WRAP_CONTENT;
    private int maxHeight = WRAP_CONTENT;
    private int marginStart, marginTop, marginEnd, marginBottom;

    protected IDialog.OnDismissListener onDismissListener;

    private Fragment preFragment;

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
        preFragment = (Fragment) getPreFragment();
        FrameLayout flContainer = findViewById(R.id._dialog_fl_container);
        this.rootView = flContainer;

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

        implView = (ViewGroup) getLayoutInflater().inflate(getImplLayoutId(), null, false);
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

//    @Override
//    public void onEnterAnimationEnd(Bundle savedInstanceState) {
//        super.onEnterAnimationEnd(savedInstanceState);
//        rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!cancelable || !cancelableInTouchOutside) {
//                    return;
//                }
//                pop();
//            }
//        });
//        rootView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRootView().getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        doShowAnimation();
                        if (preFragment != null) {
                            preFragment.onPause();
                        }
                    }
                });
    }

//    @Override
//    public void onSupportVisible() {
//        super.onSupportVisible();
//        if (preFragment != null) {
//            preFragment.onSupportVisible();
//        }
//    }

//    @Override
//    public void onSupportInvisible() {
//        super.onSupportInvisible();
//        if (preFragment != null) {
//            preFragment.onSupportInvisible();
//        }
//    }

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
        if (preFragment != null) {
            preFragment.onResume();
        }
        this.isDismissing = false;
        super.onDestroy();
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
        onBeforeShow();
        fragment.start(this);
        return this;
    }

    public BaseDialogFragment show(Context context) {
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
        return this;
    }

    public BaseDialogFragment show(SupportActivity activity) {
        onBeforeShow();
        activity.start(this);
        return this;
    }

    public void doShowAnimation() {
        popupContentAnimator = getDialogAnimator(implView);
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

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        Toast.makeText(context, "onCreateAnimation enter=" + enter + " ", Toast.LENGTH_SHORT).show();
//        if (!enter) {
//            doDismissAnimation();
//        }
//        return super.onCreateAnimation(transit, enter, nextAnim);
//    }

    public void dismiss() {
        postOnEnterAnimationEnd(() -> {
            if (!isDismissing) {
                isDismissing = true;
                doDismissAnimation();
                BaseDialogFragment.super.popThis();
                onDismiss();
            }
        });

//        if (!isDismissing) {
//            isDismissing = true;
//            doDismissAnimation();
//            super.popThis();
//            onDismiss();
////            postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    onDismiss();
////                }
////            }, 250);
//
////            postDelayed(() -> {
////                BaseDialogFragment.super.popThis();
////                onDismiss();
////            }, XPopup.getAnimationDuration());
//        }
    }

//    public void dismissWithStart(ISupportFragment fragment) {
//        if (!isDismissing) {
//            isDismissing = true;
////            doDismissAnimation();
//            if (implView != null) {
//
//                implView.animate()
//                        .alpha(0)
//                        .setDuration(90)
//                        .setInterpolator(new DecelerateInterpolator(2f))
//                        .start();
//            }
//            if (shadowBgAnimator != null) {
//                shadowBgAnimator.animateDismiss();
//            }
//            super.startWithPop(fragment);
//            onDismiss();
//        }
//    }

//    public void showFromHide() {
//
//    }
//
//    public void hide() {
//        doDismissAnimation();
//        FragmentManager manager = getFragmentManager();
//        if (manager != null) {
//            manager.beginTransaction()
//                    .hide(this)
//                    .commit();
//        }
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onHide();
//            }
//        }, 250);
//    }

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

    public BaseDialogFragment setDialogBackground(Drawable bgDrawable) {
        this.bgDrawable = bgDrawable;
        return this;
    }

    public BaseDialogFragment setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public BaseDialogFragment setMaxWidth(int maxWidth) {
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
        return this;
    }

    public BaseDialogFragment setMaxHeight(int maxHeight) {
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
        return this;
    }

    public BaseDialogFragment setMarginStart(int marginStart) {
        this.marginStart = marginStart;
        return this;
    }

    public BaseDialogFragment setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    public BaseDialogFragment setMarginEnd(int marginEnd) {
        this.marginEnd = marginEnd;
        return this;
    }

    public BaseDialogFragment setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }

    public BaseDialogFragment setMarginHorizontal(int margin) {
        setMarginStart(margin);
        setMarginEnd(margin);
        return this;
    }

    public BaseDialogFragment setMarginVertical(int margin) {
        setMarginTop(margin);
        setMarginBottom(margin);
        return this;
    }

    public BaseDialogFragment setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public BaseDialogFragment setCancelableInTouchOutside(boolean cancelableInTouchOutside) {
        this.cancelableInTouchOutside = cancelableInTouchOutside;
        return this;
    }

    public BaseDialogFragment setOnDismissListener(IDialog.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

//    public BaseDialogFragment setBackgroundDrawable(Drawable drawable) {
//        this.bgDrawable = drawable;
//        return this;
//    }

}

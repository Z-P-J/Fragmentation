package com.zpj.fragmentation.dialog.impl;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpj.fragmentation.dialog.IDialog;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.base.CardDialogFragment;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.fragmentation.dialog.widget.CustomScrollView;
import com.zpj.utils.ContextUtils;
import com.zpj.utils.ScreenUtils;

public class AlertDialogFragment extends CardDialogFragment
        implements View.OnClickListener {

    protected TextView tv_title, tv_cancel, tv_confirm, tv_neutral;
    protected String title, cancelText, neutralText, confirmText;
    protected int positionBtnColor, neutralBtnColor, negativeBtnColor;

    protected CharSequence content;

    protected View contentView;

    protected IDialog.OnButtonClickListener<AlertDialogFragment> cancelListener;
    protected IDialog.OnButtonClickListener<AlertDialogFragment> confirmListener;
    protected IDialog.OnButtonClickListener<AlertDialogFragment> onNeutralButtonClickListener;

    protected boolean isHideCancel = false;
    protected boolean autoDismiss = true;

    protected OnViewCreateListener onViewCreateListener;

    @Override
    protected int getContentLayoutId() {
        return R.layout._dialog_layout_center_impl_alert;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        FrameLayout flContent = findViewById(R.id.fl_content);
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_neutral = findViewById(R.id.tv_neutral);
        if (onNeutralButtonClickListener != null) {
            tv_neutral.setVisibility(View.VISIBLE);
        }
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_title.setTextColor(DialogThemeUtils.getMajorTextColor(context));

//        LinearLayout llButtons = findViewById(R.id.ll_buttons);

        if (contentView == null && !TextUtils.isEmpty(content)) {
            this.contentView = createContentView(content);
        }

        if (contentView != null) {
            flContent.addView(contentView);
            if (onViewCreateListener != null) {
                onViewCreateListener.onViewCreate(this, contentView);
            }
        }

        applyPrimaryColor();

        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        tv_neutral.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(cancelText)) {
            tv_cancel.setText(cancelText);
        }
        if (!TextUtils.isEmpty(confirmText)) {
            tv_confirm.setText(confirmText);
        }
        if (!TextUtils.isEmpty(neutralText)) {
            tv_neutral.setText(neutralText);
        }

        if (isHideCancel) tv_cancel.setVisibility(View.GONE);

        View shadowBottomView = findViewById(R.id.view_shadow_bottom);
        View shadowUpView = findViewById(R.id.view_shadow_up);

        if (shadowBottomView != null && shadowUpView != null) {
            CustomScrollView scrollView = findViewById(R.id._scroll_view);
            scrollView.setOnScrollChangeListener(new CustomScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollToStart() {

                }

                @Override
                public void onScrollToEnd() {

                }

                @Override
                public void onScrollChanged(int l, int t, int oldl, int oldt) {
                    shadowBottomView.setVisibility(scrollView.isScrollToTop() ? View.GONE : View.VISIBLE);
                    shadowUpView.setVisibility(scrollView.isScrollToBottom() ? View.GONE : View.VISIBLE);
                }
            });

            postOnEnterAnimationEnd(new Runnable() {
                @Override
                public void run() {
                    shadowBottomView.setVisibility(scrollView.isScrollToTop() ? View.GONE : View.VISIBLE);
                    shadowUpView.setVisibility(scrollView.isScrollToBottom() ? View.GONE : View.VISIBLE);
                }
            });
        }



    }

    @Override
    public void onDismiss() {
        super.onDismiss();
//        if (runnable != null) {
//            runnable.run();
//        }
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            if (cancelListener != null) {
                cancelListener.onClick(this, IDialog.BUTTON_NEGATIVE);
            }
            if (autoDismiss) {
                dismiss();
            }

        } else if (v == tv_confirm) {
            if (confirmListener != null) {
                confirmListener.onClick(this, IDialog.BUTTON_POSITIVE);
            }
            if (autoDismiss) {
                dismiss();
            }

        }  else if (v == tv_neutral) {
            if (onNeutralButtonClickListener != null) {
                onNeutralButtonClickListener.onClick(this, IDialog.BUTTON_NEUTRAL);
            }
            if (autoDismiss) {
                dismiss();
            }
        }
    }

    protected View createContentView(CharSequence content) {
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextColor(DialogThemeUtils.getNormalTextColor(context));
        textView.setTextSize(14);
        int padding = ScreenUtils.dp2pxInt(context, 24);
        textView.setPadding(padding, padding / 3, padding, padding / 3);
        textView.setMinLines(3);
        textView.setLineSpacing(6, 1);
        return textView;
    }

    protected void applyPrimaryColor() {
        if (positionBtnColor == 0) {
            tv_confirm.setTextColor(DialogThemeUtils.getPositiveTextColor(context));
        } else {
            tv_confirm.setTextColor(positionBtnColor);
        }

        if (neutralBtnColor == 0) {
            tv_cancel.setTextColor(DialogThemeUtils.getNegativeTextColor(context));
        } else {
            tv_neutral.setTextColor(neutralBtnColor);
        }

        if (negativeBtnColor == 0) {
            tv_cancel.setTextColor(DialogThemeUtils.getNegativeTextColor(context));
        } else {
            tv_cancel.setTextColor(negativeBtnColor);
        }
    }

    public AlertDialogFragment setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        return this;
    }

    public AlertDialogFragment setPositiveButton(IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.confirmListener = listener;
        return this;
    }

    public AlertDialogFragment setPositiveButton(String btnStr, IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.confirmText = btnStr;
        this.confirmListener = listener;
        return this;
    }

    public AlertDialogFragment setPositiveButton(int btnStrId, IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.confirmText = ContextUtils.getApplicationContext().getString(btnStrId);
        this.confirmListener = listener;
        return this;
    }

    public AlertDialogFragment setNegativeButton(IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.cancelListener = listener;
        return this;
    }

    public AlertDialogFragment setNegativeButton(String btnStr, IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.cancelText = btnStr;
        this.cancelListener = listener;
        return this;
    }

    public AlertDialogFragment setNegativeButton(int btnStrId, IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.cancelText = ContextUtils.getApplicationContext().getString(btnStrId);
        this.cancelListener = listener;
        return this;
    }

    public AlertDialogFragment setNeutralButton(IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.onNeutralButtonClickListener = listener;
        return this;
    }

    public AlertDialogFragment setNeutralButton(String btnStr, IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.neutralText = btnStr;
        this.onNeutralButtonClickListener = listener;
        return this;
    }

    public AlertDialogFragment setNeutralButton(int btnStrId, IDialog.OnButtonClickListener<AlertDialogFragment> listener) {
        this.neutralText = ContextUtils.getApplicationContext().getString(btnStrId);
        this.onNeutralButtonClickListener = listener;
        return this;
    }

    public AlertDialogFragment setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    public AlertDialogFragment setContent(@StringRes int resId) {
        return setContent(ContextUtils.getApplicationContext().getResources().getString(resId));
    }

    public AlertDialogFragment setContent(@LayoutRes int resId, OnViewCreateListener listener) {
        this.contentView = LayoutInflater.from(ContextUtils.getApplicationContext()).inflate(resId, null, false);
        this.onViewCreateListener = listener;
        return this;
    }

    public AlertDialogFragment setContent(View view) {
        this.contentView = view;
        return this;
    }

    public AlertDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public AlertDialogFragment setTitle(int titleRes) {
        this.title = ContextUtils.getApplicationContext().getString(titleRes);
        return this;
    }

    public AlertDialogFragment setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public AlertDialogFragment setConfirmText(String confirmText) {
        this.confirmText = confirmText;
        return this;
    }

    public AlertDialogFragment hideCancelBtn() {
        isHideCancel = true;
        return this;
    }

    public AlertDialogFragment onViewCreate(OnViewCreateListener onViewCreateListener) {
        this.onViewCreateListener = onViewCreateListener;
        return this;
    }

    public AlertDialogFragment setPositionButtonColor(int positionBtnColor) {
        this.positionBtnColor = positionBtnColor;
        return this;
    }

    public AlertDialogFragment setNeutralButtonColor(int neutralBtnColor) {
        this.neutralBtnColor = neutralBtnColor;
        return this;
    }

    public AlertDialogFragment setNegativeButtonColor(int negativeBtnColor) {
        this.negativeBtnColor = negativeBtnColor;
        return this;
    }

    public interface OnViewCreateListener {
        void onViewCreate(AlertDialogFragment fragment, View view);
    }

//    public interface OnPositiveButtonClickListener  {
//        void onClick(AlertDialogFragment fragment);
//    }
//
//    public interface OnNegativeButtonClickListener {
//        void onClick(AlertDialogFragment fragment);
//    }

//    public interface OnButtonClickListener {
//        void onClick(AlertDialogFragment fragment);
//    }

}

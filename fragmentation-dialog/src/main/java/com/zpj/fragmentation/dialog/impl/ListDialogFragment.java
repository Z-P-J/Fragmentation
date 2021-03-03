package com.zpj.fragmentation.dialog.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpj.fragmentation.dialog.IDialog;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.base.ContainerDialogFragment;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.recyclerview.EasyRecyclerView;
import com.zpj.recyclerview.IEasy;
import com.zpj.utils.ContextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDialogFragment<T> extends ContainerDialogFragment {

    protected final List<T> list = new ArrayList<>();

    protected String title;
    protected String negativeText, neutralText, positiveText;

    protected TextView tvTitle;
    protected TextView tvOk;
    private View shadowBottomView;
    private View shadowUpView;

    protected boolean showButtons = false;
    protected boolean autoDismiss = true;

    protected int bindItemLayoutId = R.layout._dialog_item_select;

    private IEasy.OnBindViewHolderListener<T> onBindViewHolderListener;
    protected IEasy.OnItemClickListener<T> onItemClickListener;

    protected IDialog.OnButtonClickListener<ListDialogFragment<T>> negativeClickListener;
    protected IDialog.OnButtonClickListener<ListDialogFragment<T>> positiveClickListener;

    protected int getItemRes() {
        return bindItemLayoutId;
    }

    @Override
    protected final boolean isDragDialog() {
        return isDrag();
    }

    boolean isDrag() {
        return false;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout._dialog_layout_center_impl_list;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
//        if (getItemRes() <= 0) {
//            dismiss();
//            return;
//        }
        super.initView(view, savedInstanceState);

        shadowBottomView = findViewById(R.id.view_shadow_bottom);
        shadowUpView = findViewById(R.id.view_shadow_up);

        tvTitle = findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setTextColor(DialogThemeUtils.getMajorTextColor(context));
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
                shadowBottomView.setVisibility(View.GONE);
            } else {
                tvTitle.setText(title);
            }
        }

        LinearLayout buttons = findViewById(R.id.layout_buttons);
        if (showButtons) {
            buttons.setVisibility(View.VISIBLE);
            shadowUpView.setVisibility(View.VISIBLE);

            TextView tvCancel = buttons.findViewById(R.id.tv_cancel);
            if (!TextUtils.isEmpty(negativeText)) {
                tvCancel.setText(negativeText);
            }
            tvCancel.setTextColor(DialogThemeUtils.getNegativeTextColor(context));
            tvCancel.setOnClickListener(this::onNegativeButtonClick);

            tvOk = buttons.findViewById(R.id.tv_ok);
            if (TextUtils.isEmpty(positiveText)) {
                positiveText = String.valueOf(tvOk.getText());
            }
            tvOk.setText(positiveText);
            tvOk.setTextColor(DialogThemeUtils.getPositiveTextColor(context));
            tvOk.setOnClickListener(this::onPositiveButtonClick);
        } else {
            buttons.setVisibility(View.GONE);
            shadowUpView.setVisibility(View.GONE);

            FrameLayout flContainer = findViewById(R.id._fl_container);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flContainer.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.weight = 0;

        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                test(recyclerView);
            }
        });
        initRecyclerView(recyclerView, list);

//        recyclerView.getViewTreeObserver()
//                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        recyclerView.getViewTreeObserver()
//                                .removeOnGlobalLayoutListener(this);
//                        ListDialogFragment.super.doShowAnimation();
//                    }
//                });
        recyclerView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        recyclerView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        ListDialogFragment.super.doShowAnimation();
                        postOnEnterAnimationEnd(() -> test(recyclerView));
                        return false;
                    }
                });
    }

    private void test(RecyclerView recyclerView) {
        if (!recyclerView.canScrollVertically(-1)) {
            shadowBottomView.setVisibility(View.GONE);
        } else {
            shadowBottomView.setVisibility(View.VISIBLE);
        }
        if (showButtons) {
            if (!recyclerView.canScrollVertically(1)) {
                shadowUpView.setVisibility(View.GONE);
            } else {
                shadowUpView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void initRecyclerView(RecyclerView recyclerView, List<T> list) {
        new EasyRecyclerView<T>(recyclerView)
                .setData(list)
                .setItemRes(getItemRes())
                .onBindViewHolder(onBindViewHolderListener)
                .onItemClick(onItemClickListener)
                .build();
    }

    @Override
    public void doShowAnimation() {

    }

    protected void onNegativeButtonClick(View view) {
        if (negativeClickListener != null) {
            negativeClickListener.onClick(this, IDialog.BUTTON_NEGATIVE);
        }
        if (autoDismiss) {
            dismiss();
        }
    }

    protected void onPositiveButtonClick(View view) {
        if (positiveClickListener != null) {
            positiveClickListener.onClick(this, IDialog.BUTTON_POSITIVE);
        }
        if (autoDismiss) {
            dismiss();
        }
    }

    public ListDialogFragment<T> setItemLayoutId(int itemLayoutId) {
        this.bindItemLayoutId = itemLayoutId;
        return this;
    }

    public ListDialogFragment<T> setShowButtons(boolean showButtons) {
        this.showButtons = showButtons;
        return this;
    }

    public ListDialogFragment<T> setPositiveButton(IDialog.OnButtonClickListener<ListDialogFragment<T>> listener) {
        this.positiveClickListener = listener;
        return this;
    }

    public ListDialogFragment<T> setPositiveButton(String btnStr, IDialog.OnButtonClickListener<ListDialogFragment<T>> listener) {
        this.positiveText = btnStr;
        this.positiveClickListener = listener;
        return this;
    }

    public ListDialogFragment<T> setPositiveButton(int btnStrId, IDialog.OnButtonClickListener<ListDialogFragment<T>> listener) {
        this.positiveText = ContextUtils.getApplicationContext().getString(btnStrId);
        this.positiveClickListener = listener;
        return this;
    }

    public ListDialogFragment<T> setNegativeButton(IDialog.OnButtonClickListener<ListDialogFragment<T>> listener) {
        this.negativeClickListener = listener;
        return this;
    }

    public ListDialogFragment<T> setNegativeButton(String btnStr, IDialog.OnButtonClickListener<ListDialogFragment<T>> listener) {
        this.negativeText = btnStr;
        this.negativeClickListener = listener;
        return this;
    }

    public ListDialogFragment<T> setNegativeButton(int btnStrId, IDialog.OnButtonClickListener<ListDialogFragment<T>> listener) {
        this.negativeText = ContextUtils.getApplicationContext().getString(btnStrId);
        this.negativeClickListener = listener;
        return this;
    }

    public ListDialogFragment<T> setNegativeText(String negativeText) {
        this.negativeText = negativeText;
        return this;
    }

    public ListDialogFragment<T> setPositiveText(String positiveText) {
        this.positiveText = positiveText;
        return this;
    }

    public ListDialogFragment<T> setTitle(String title) {
        this.title = title;
        return this;
    }

    public ListDialogFragment<T> setData(List<T> data) {
        this.list.clear();
        return addData(data);
    }

    public ListDialogFragment<T> setData(T...datas) {
        this.list.clear();
        return addData(datas);
    }

    public ListDialogFragment<T> addData(List<T> data) {
        this.list.addAll(data);
        return this;
    }

    public ListDialogFragment<T> addData(T...datas) {
        this.list.addAll(Arrays.asList(datas));
        return this;
    }

    public ListDialogFragment<T> onBindViewHolder(IEasy.OnBindViewHolderListener<T> onBindViewHolderListener) {
        this.onBindViewHolderListener = onBindViewHolderListener;
        return this;
    }

    public ListDialogFragment<T> setOnItemClickListener(IEasy.OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

}

package com.zpj.fragmentation.dialog.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zpj.fragmentation.dialog.IDialog;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.recyclerview.EasyRecyclerView;
import com.zpj.recyclerview.EasyViewHolder;
import com.zpj.recyclerview.IEasy;
import com.zpj.widget.checkbox.SmoothCheckBox;

import java.util.ArrayList;
import java.util.List;

public class SelectDialogFragment<T> extends ListDialogFragment<T>
        implements IEasy.OnBindViewHolderListener<T> {

    public interface OnMultiSelectListener<T> {
        void onSelect(SelectDialogFragment<T> dialog, List<Integer> selected, List<T> list);
    }

    public interface OnSingleSelectListener<T> {
        void onSelect(SelectDialogFragment<T> dialog, int position, T item);
    }

//    public interface IconCallback<T> {
//        void onSetIcon(ImageView icon, T item, int position);
//    }
//
//    public interface TitleCallback<T> {
//        void onSetTitle(TextView titleView, T item, int position);
//    }
//
//    public interface SubtitleCallback<T> {
//        void onSetSubtitle(TextView subtitleView, T item, int position);
//    }

    private final List<Integer> selectedList = new ArrayList<>();

    private EasyRecyclerView<T> recyclerView;

    private boolean isSelectMode = false;
    private boolean isMultiple = false;


    private OnSingleSelectListener<T> onSingleSelectListener;
    private OnMultiSelectListener<T> onMultiSelectListener;
    private IDialog.ViewBinder<ImageView, T> iconCallback;
    private IDialog.ViewBinder<TextView, T> titleCallback;
    private IDialog.ViewBinder<TextView, T> subtitleCallback;

    private SmoothCheckBox selectAllCheckBox;

    @Override
    protected final int getItemRes() {
        return R.layout._dialog_item_select;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        if (isMultiple || onMultiSelectListener != null || selectedList.size() > 0
                || onSingleSelectListener != null) {
            isSelectMode = true;
        }
        if (isMultiple) {
            showButtons = true;
        }
        super.initView(view, savedInstanceState);

        FrameLayout flCheckBox = contentView.findViewById(R.id.fl_check_box);
        selectAllCheckBox = contentView.findViewById(R.id.check_box);

        if (isMultiple) {
            flCheckBox.setVisibility(View.VISIBLE);
            selectAllCheckBox.setCheckedColor(DialogThemeUtils.getColorPrimary(context));
            View.OnClickListener listener = v -> {
                boolean selectAll = !selectAllCheckBox.isChecked();
                selectedList.clear();
                if (selectAll) {
                    for (int i = 0; i < list.size(); i++) {
                        selectedList.add(i);
                    }
                }
                recyclerView.notifyDataSetChanged();
                updateOkButton();
            };
            selectAllCheckBox.setOnClickListener(listener);
            flCheckBox.setOnClickListener(listener);
        } else {
            flCheckBox.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onPositiveButtonClick(View view) {
        if (isSelectMode) {
            if (isMultiple && onMultiSelectListener != null) {
                onMultiSelectListener.onSelect(this, selectedList, list);
            } else if (!isMultiple && onSingleSelectListener != null) {
                onSingleSelectListener.onSelect(this, selectedList.get(0), list.get(selectedList.get(0)));
            }
        }
        if (showButtons) {
            super.onPositiveButtonClick(view);
        } else if (autoDismiss) {
            dismiss();
        }

    }

    @Override
    protected void initRecyclerView(RecyclerView recyclerView, List<T> list) {
        this.recyclerView = new EasyRecyclerView<T>(recyclerView);
        this.recyclerView.setData(list)
                .setItemRes(getItemRes())
                .onBindViewHolder(this)
                .build();
    }

    @Override
    public void onBindViewHolder(EasyViewHolder holder, List<T> list, int position, List<Object> payloads) {
        ImageView iconView = holder.getView(R.id.icon_view);
        TextView titleView = holder.getView(R.id.title_view);
        titleView.setTextColor(DialogThemeUtils.getMajorTextColor(context));
        TextView contentView = holder.getView(R.id.content_view);
        contentView.setTextColor(DialogThemeUtils.getNormalTextColor(context));
        final SmoothCheckBox checkBox = holder.getView(R.id.check_box);
        checkBox.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
        checkBox.setCheckedColor(DialogThemeUtils.getColorPrimary(context));
        checkBox.setChecked(selectedList.contains(position), false);
        holder.setOnItemClickListener(v -> {
            if (isSelectMode) {
                if (isMultiple) {
                    if (checkBox.isChecked()) {
                        unSelect(holder.getAdapterPosition());
                    } else {
                        onSelected(holder.getAdapterPosition());
                    }
                    checkBox.setChecked(!checkBox.isChecked(), true);
                } else {
                    if (!checkBox.isChecked()) {
                        if (selectedList.size() > 0) {
                            int selected = selectedList.get(0);
                            selectedList.clear();
                            recyclerView.notifyItemChanged(selected);
                        }
                        onSelected(holder.getAdapterPosition());
                        checkBox.setChecked(true, true);
                        if (!showButtons) {
                            onPositiveButtonClick(tvOk);
                        }
                    }
                }
            } else if (onItemClickListener != null) {
                onItemClickListener.onClick(holder, v, list.get(holder.getAdapterPosition()));
            }

        });
        if (iconCallback == null) {
            iconView.setVisibility(View.GONE);
        } else {
            iconView.setVisibility(View.VISIBLE);
            iconCallback.onBindView(iconView, list.get(position), position);
        }
        if (titleCallback == null) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
            titleCallback.onBindView(titleView, list.get(position), position);
        }
        if (subtitleCallback == null) {
            contentView.setVisibility(View.GONE);
        } else {
            contentView.setVisibility(View.VISIBLE);
            subtitleCallback.onBindView(contentView, list.get(position), position);
        }
    }

    public SelectDialogFragment<T> setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        return this;
    }

    public SelectDialogFragment<T> setMultiple(boolean isMultiple) {
        this.isMultiple = isMultiple;
        return this;
    }

    public SelectDialogFragment<T> setSelected(int[] selected) {
        for (int position : selected) {
            onSelected(position);
        }
        if (selected.length > 1) {
            isMultiple = true;
        }
        return this;
    }

    public SelectDialogFragment<T> setSelected(int selected) {
        onSelected(selected);
        return this;
    }

    public SelectDialogFragment<T> onSingleSelect(OnSingleSelectListener<T> onSingleSelectListener) {
        isMultiple = false;
        this.onSingleSelectListener = onSingleSelectListener;
        return this;
    }

    public SelectDialogFragment<T> onMultiSelect(OnMultiSelectListener<T> onMultiSelectListener) {
        isMultiple = true;
        showButtons = true;
        this.onMultiSelectListener = onMultiSelectListener;
        return this;
    }

    public SelectDialogFragment<T> onBindIcon(IDialog.ViewBinder<ImageView, T> iconCallback) {
        this.iconCallback = iconCallback;
        return this;
    }

    public SelectDialogFragment<T> onBindTitle(IDialog.ViewBinder<TextView, T> titleCallback) {
        this.titleCallback = titleCallback;
        return this;
    }

    public SelectDialogFragment<T> onBindSubtitle(IDialog.ViewBinder<TextView, T> subtitleCallback) {
        this.subtitleCallback = subtitleCallback;
        return this;
    }


    private void onSelected(int position) {
        if (!selectedList.contains(position)) {
            selectedList.add(position);
            updateOkButton();
        }
    }

    private void unSelect(int position) {
        selectedList.remove(Integer.valueOf(position));
        updateOkButton();
    }

    private void updateOkButton() {
        if (isMultiple) {
            if (selectedList.size() == list.size()) {
                if (!selectAllCheckBox.isChecked()) {
                    selectAllCheckBox.setChecked(true, true);
                }
            } else {
                if (selectAllCheckBox.isChecked()) {
                    selectAllCheckBox.setChecked(false, true);
                }
            }
            if (selectedList.isEmpty()) {
                tvOk.setText(positiveText);
            } else {
                tvOk.setText(positiveText + "(" + selectedList.size() + ")");
            }

        }
    }

}
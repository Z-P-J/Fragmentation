package com.zpj.fragmentation.dialog;

import android.view.View;

import com.zpj.fragmentation.dialog.base.BaseDialogFragment;

import java.util.List;

public interface IDialog {

    interface OnDismissListener {
        void onDismiss();
    }

    /** The identifier for the positive button. */
    int BUTTON_POSITIVE = -1;

    /** The identifier for the negative button. */
    int BUTTON_NEGATIVE = -2;

    /** The identifier for the neutral button. */
    int BUTTON_NEUTRAL = -3;

    interface OnButtonClickListener<T extends BaseDialogFragment<T>> {
        void onClick(T fragment, int which);
    }

    interface ViewBinder<V extends View, T> {
        void onBindView(V view, T item, int position);
    }

    interface OnViewCreateListener<T> {
        void onViewCreate(T fragment, View view);
    }

    public interface OnMultiSelectListener<T, S> {
        void onSelect(S dialog, List<Integer> selected, List<T> list);
    }

    public interface OnSingleSelectListener<T, S> {
        void onSelect(S dialog, int position, T item);
    }

}

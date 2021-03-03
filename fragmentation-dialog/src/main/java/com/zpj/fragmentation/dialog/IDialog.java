package com.zpj.fragmentation.dialog;

import android.view.View;

import com.zpj.fragmentation.dialog.base.BaseDialogFragment;
import com.zpj.fragmentation.dialog.impl.AlertDialogFragment;

public interface IDialog {

    interface OnCancelListener {
        void onCancel(AlertDialogFragment fragment);
    }

    interface OnConfirmListener {
        void onConfirm(AlertDialogFragment fragment);
    }

    interface OnDismissListener {
        void onDismiss();
    }

    /** The identifier for the positive button. */
    int BUTTON_POSITIVE = -1;

    /** The identifier for the negative button. */
    int BUTTON_NEGATIVE = -2;

    /** The identifier for the neutral button. */
    int BUTTON_NEUTRAL = -3;

    interface OnButtonClickListener<T extends BaseDialogFragment> {
        void onClick(T fragment, int which);
    }

    public interface ViewBinder<V extends View, T> {
        void onBindView(V view, T item, int position);
    }

}

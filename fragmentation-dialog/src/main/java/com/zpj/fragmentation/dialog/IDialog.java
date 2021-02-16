package com.zpj.fragmentation.dialog;

import android.view.View;

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

}

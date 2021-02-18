package com.zpj.fragmentation.dialog.base;

import android.view.ViewGroup;

import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.animator.ScaleAlphaAnimator;
import com.zpj.fragmentation.dialog.enums.PopupAnimation;

public abstract class CardDialogFragment extends ContainerDialogFragment {

    @Override
    protected final boolean isDragDialog() {
        return false;
    }

}

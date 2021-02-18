package com.zpj.fragmentation.dialog.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.fragmentation.anim.DefaultNoAnimator;
import com.zpj.fragmentation.dialog.R;
import com.zpj.fragmentation.dialog.animator.PopupAnimator;
import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
import com.zpj.fragmentation.dialog.widget.SmartDragLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class BottomDragDialogFragment extends ContainerDialogFragment {

    @Override
    protected final boolean isDragDialog() {
        return true;
    }

}

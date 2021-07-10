package com.zpj.fragmentation.dialog.impl;

public class BottomDragSelectDialogFragment<T>
        extends SelectDialogFragment<T, BottomDragSelectDialogFragment<T>> {

    @Override
    final boolean isDrag() {
        return true;
    }
}

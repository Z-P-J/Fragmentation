package com.zpj.fragmentation.dialog.impl;

public class BottomDragListDialogFragment<T> extends ListDialogFragment<T, BottomDragListDialogFragment<T>> {

    @Override
    final boolean isDrag() {
        return true;
    }

}

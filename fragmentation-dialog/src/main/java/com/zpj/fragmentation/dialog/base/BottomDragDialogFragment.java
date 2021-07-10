package com.zpj.fragmentation.dialog.base;

public abstract class BottomDragDialogFragment<T extends BottomDragDialogFragment<T>>
        extends ContainerDialogFragment<T> {

    @Override
    protected final boolean isDragDialog() {
        return true;
    }

}

package com.zpj.fragmentation.dialog.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zpj.popup.R;
import com.zpj.popup.animator.PopupAnimator;
import com.zpj.popup.util.XPopupUtils;
import com.zpj.popup.widget.SmartDragLayout;
import com.zpj.utils.ScreenUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BottomDialogFragment extends BaseDialogFragment {

    protected SmartDragLayout bottomPopupContainer;

    private View implView;

    protected Boolean enableDrag = true;;

    @Override
    protected final int getImplLayoutId() {
        return R.layout._dialog_layout_bottom_view;
    }

    protected abstract int getContentLayoutId();

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
        return null;
    }

    @Override
    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
        return null;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);

        bottomPopupContainer = findViewById(R.id.bottomPopupContainer);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) bottomPopupContainer.getLayoutParams();
        layoutParams.height = MATCH_PARENT;
        layoutParams.width = MATCH_PARENT;


        implView = LayoutInflater.from(context).inflate(getContentLayoutId(), null, false);
        bottomPopupContainer.addView(implView);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) implView.getLayoutParams();
        params.height = WRAP_CONTENT;
        params.width = getMaxWidth();
        params.gravity = Gravity.BOTTOM;

        bottomPopupContainer.enableDrag(enableDrag);
        bottomPopupContainer.dismissOnTouchOutside(true);
        bottomPopupContainer.handleTouchOutsideEvent(true);
        bottomPopupContainer.hasShadowBg(true);

        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
            @Override
            public void onClose() {
                dismiss();
            }
            @Override
            public void onOpen() {

            }
        });

        bottomPopupContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        XPopupUtils.applyPopupSize((ViewGroup) getImplView(), getMaxWidth(), 0);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void doShowAnimation() {
        super.doShowAnimation();
        bottomPopupContainer.open();
    }

    @Override
    public void doDismissAnimation() {
        super.doDismissAnimation();
        bottomPopupContainer.close();
    }

    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth(context));
    }

}

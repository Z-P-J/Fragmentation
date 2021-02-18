//package com.zpj.fragmentation.dialog.base;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import com.zpj.fragmentation.anim.DefaultNoAnimator;
//import com.zpj.fragmentation.dialog.R;
//import com.zpj.fragmentation.dialog.animator.PopupAnimator;
//import com.zpj.fragmentation.dialog.utils.DialogThemeUtils;
//import com.zpj.fragmentation.dialog.widget.SmartDragLayout;
//
//import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
//
//public abstract class BottomDragDialogFragment_old extends BaseDialogFragment {
//
//    protected SmartDragLayout bottomPopupContainer;
//
//    private ViewGroup contentView;
//
//    protected Boolean enableDrag = true;
//
//    public BottomDragDialogFragment_old() {
//        setMaxWidth(MATCH_PARENT);
//    }
//
//    @Override
//    protected final int getImplLayoutId() {
//        return R.layout._dialog_layout_bottom_view;
//    }
//
//    protected abstract int getContentLayoutId();
//
//    @Override
//    protected int getGravity() {
//        return Gravity.BOTTOM;
//    }
//
//    @Override
//    protected PopupAnimator getDialogAnimator(ViewGroup contentView) {
//        return null;
//    }
//
//    @Override
//    protected PopupAnimator getShadowAnimator(FrameLayout flContainer) {
//        return null;
//    }
//
//    @Override
//    protected void initView(View view, @Nullable Bundle savedInstanceState) {
//        super.initView(view, savedInstanceState);
//
//        bottomPopupContainer = (SmartDragLayout) getImplView();
////        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) bottomPopupContainer.getLayoutParams();
////        layoutParams.height = MATCH_PARENT;
////        layoutParams.width = MATCH_PARENT;
//
//
//        contentView = (ViewGroup) getLayoutInflater().inflate(getContentLayoutId(), null, false);
//        if (getMarginTop() > 0 || getMarginBottom() > 0) {
//            FrameLayout flContainer = new FrameLayout(context);
//            flContainer.addView(contentView);
//            bottomPopupContainer.addView(flContainer);
//        } else {
//            bottomPopupContainer.addView(contentView);
//        }
//
//        if (bgDrawable != null) {
//            contentView.setBackground(bgDrawable);
//        } else {
//            contentView.setBackground(DialogThemeUtils.getBottomDialogBackground(context));
//        }
//
//        super.initLayoutParams(contentView);
//        bottomPopupContainer.bindContentView(contentView);
//
//        bottomPopupContainer.enableDrag(enableDrag);
//        bottomPopupContainer.dismissOnTouchOutside(true);
//        bottomPopupContainer.handleTouchOutsideEvent(true);
//        bottomPopupContainer.hasShadowBg(true);
//
//        bottomPopupContainer.setOnCloseListener(new SmartDragLayout.OnCloseListener() {
//            @Override
//            public void onClose() {
//                setFragmentAnimator(new DefaultNoAnimator());
//                postOnEnterAnimationEnd(() -> {
//                    BottomDragDialogFragment_old.super.doDismissAnimation();
//                    popThis();
//                    onDismiss();
//                });
//            }
//            @Override
//            public void onOpen() {
//
//            }
//        });
//
//        bottomPopupContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//    }
//
//    @Override
//    protected void initLayoutParams(ViewGroup view) {
//
//    }
//
//    @Override
//    public void doShowAnimation() {
//        super.doShowAnimation();
//        bottomPopupContainer.open();
//    }
//
//    @Override
//    public void doDismissAnimation() {
//        super.doDismissAnimation();
//        bottomPopupContainer.close();
//    }
//
//    public BottomDragDialogFragment_old setEnableDrag(Boolean enableDrag) {
//        this.enableDrag = enableDrag;
//        return this;
//    }
//}

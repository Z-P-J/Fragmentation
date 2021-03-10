package com.zpj.fragmentation.dialog.animator;

import android.view.View;

import com.zpj.fragmentation.dialog.enums.PopupAnimation;

/**
 * Description: 弹窗动画执行器
 * Create by dance, at 2018/12/9
 */
public abstract class PopupAnimator {

    protected long showDuration = 360;
    protected long dismissDuration = 360;

    public View targetView;
    public PopupAnimation popupAnimation; // 内置的动画
    public PopupAnimator(){}
    public PopupAnimator(View target){
        this(target, null);
    }

    public PopupAnimator(View target, PopupAnimation popupAnimation){
        this.targetView = target;
        this.popupAnimation = popupAnimation;
    }

    public void setShowDuration(long showAnimDuration) {
        this.showDuration = showAnimDuration;
    }

    public void setDismissDuration(long dismissAnimDuration) {
        this.dismissDuration = dismissAnimDuration;
    }

    public long getShowDuration() {
        return showDuration;
    }

    public long getDismissDuration() {
        return dismissDuration;
    }

    public abstract void initAnimator();
    public abstract void animateShow();
    public abstract void animateDismiss();
//    public int getDuration(){
//        return DialogConfig.getAnimationDuration();
//    }
}

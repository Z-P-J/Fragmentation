package com.zpj.fragmentation;

import android.view.MotionEvent;

import com.zpj.fragmentation.anim.FragmentAnimator;

/**
 * Created by YoKey on 17/6/13.
 * Modified by Z-P-J
 */

public interface ISupportActivity extends ISupport<SupportActivityDelegate> {

    void onBackPressedSupport();

    boolean dispatchTouchEvent(MotionEvent ev);
}

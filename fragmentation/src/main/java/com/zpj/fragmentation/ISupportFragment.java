package com.zpj.fragmentation;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.zpj.fragmentation.anim.FragmentAnimator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YoKey on 17/6/23.
 * Modified by Z-P-J
 */

public interface ISupportFragment extends ISupport<SupportFragmentDelegate> {
    // LaunchMode
    int STANDARD = 0;
    int SINGLETOP = 1;
    int SINGLETASK = 2;

    // ResultCode
    int RESULT_CANCELED = 0;
    int RESULT_OK = -1;

    @IntDef({STANDARD, SINGLETOP, SINGLETASK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LaunchMode {
    }

//    SupportFragmentDelegate getSupportDelegate();
//
//    ExtraTransaction extraTransaction();
//
//    void enqueueAction(Runnable runnable);
//
//    void post(Runnable runnable);
//
//    void postDelayed(Runnable runnable, long delay);

    void onEnterAnimationEnd(@Nullable Bundle savedInstanceState);

    void onLazyInitView(@Nullable Bundle savedInstanceState);

    void onSupportVisible();

    void onSupportInvisible();

    boolean isSupportVisible();

    boolean isLazyInit();

    FragmentAnimator onCreateFragmentAnimator();

//    FragmentAnimator getFragmentAnimator();
//
//    void setFragmentAnimator(FragmentAnimator fragmentAnimator);

    void setFragmentResult(int resultCode, Bundle bundle);

    void onFragmentResult(int requestCode, int resultCode, Bundle data);

    void onNewBundle(Bundle args);

    void putNewBundle(Bundle newBundle);

    boolean onBackPressedSupport();

    @LaunchMode int getLaunchMode();

//    Handler getHandler();



}
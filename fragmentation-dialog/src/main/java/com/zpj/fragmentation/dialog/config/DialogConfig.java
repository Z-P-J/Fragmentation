package com.zpj.fragmentation.dialog.config;

import android.graphics.Color;

public class DialogConfig {

    private DialogConfig() {

    }

    private static int animationDuration = 360; // 360
    public static int statusBarShadowColor = Color.parseColor("#55000000");

    public static int getAnimationDuration() {
        return animationDuration;
    }
}

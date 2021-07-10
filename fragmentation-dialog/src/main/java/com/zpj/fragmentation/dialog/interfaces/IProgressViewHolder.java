package com.zpj.fragmentation.dialog.interfaces;

import android.content.Context;
import android.view.View;

public interface IProgressViewHolder<T extends View> {

    T createProgressView(Context context);

    void onProgressChanged(T progressView, float progress);

}

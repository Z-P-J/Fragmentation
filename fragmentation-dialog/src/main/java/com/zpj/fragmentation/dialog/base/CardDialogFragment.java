package com.zpj.fragmentation.dialog.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;

import com.zpj.fragmentation.dialog.R;

public abstract class CardDialogFragment extends ContainerDialogFragment {

    private boolean transparent = false;

    @Override
    protected final boolean isDragDialog() {
        return false;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        if (transparent) {
            CardView cardView = findViewById(R.id._centerPopupContainer);
            cardView.setCardElevation(0);
            cardView.setUseCompatPadding(false);
            cardView.setCardBackgroundColor(Color.TRANSPARENT);
        }
    }

    public CardDialogFragment setTransparentBackground(boolean transparent) {
        this.transparent = transparent;
        return this;
    }
}

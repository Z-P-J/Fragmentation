package com.zpj.fragmentation.dialog.impl;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zpj.fragmentation.dialog.IDialog;

import java.util.ArrayList;
import java.util.List;

public class SimpleSelectDialogFragment extends SelectDialogFragment<String> {

    private final List<String> subTitles = new ArrayList<>();
    private final List<Integer> iconIds = new ArrayList<>();

    public SimpleSelectDialogFragment() {
        onBindTitle(new IDialog.ViewBinder<TextView, String>() {
            @Override
            public void onBindView(TextView titleView, String item, int position) {
                titleView.setText(item);
            }
        });
        onBindSubtitle(new IDialog.ViewBinder<TextView, String>() {
            @Override
            public void onBindView(TextView subtitleView, String item, int position) {
                if (!subTitles.isEmpty() && position < subTitles.size()) {
                    subtitleView.setText(subTitles.get(position));
                } else  {
                    subtitleView.setVisibility(View.GONE);
                }
            }
        });
        onBindIcon(new IDialog.ViewBinder<ImageView, String>() {
            @Override
            public void onBindView(ImageView icon, String item, int position) {
                if (!iconIds.isEmpty() && position < iconIds.size()) {
                    icon.setImageResource(iconIds.get(position));
                } else {
                    icon.setVisibility(View.GONE);
                }
            }
        });
    }

    @Deprecated
    @Override
    public SimpleSelectDialogFragment setData(List<String> data) {
        super.setData(data);
        return this;
    }

    public SimpleSelectDialogFragment setTitles(List<String> data) {
        super.setData(data);
        return this;
    }

    public SimpleSelectDialogFragment setSubtitles(List<String> subTitles) {
        this.subTitles.addAll(subTitles);
        return this;
    }

    public SimpleSelectDialogFragment setIconIds(List<Integer> iconIds) {
        this.iconIds.addAll(iconIds);
        return this;
    }

}

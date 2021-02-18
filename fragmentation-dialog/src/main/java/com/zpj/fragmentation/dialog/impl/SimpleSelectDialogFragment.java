package com.zpj.fragmentation.dialog.impl;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SimpleSelectDialogFragment extends SelectDialogFragment<String> {

    private final List<String> subTitles = new ArrayList<>();
    private final List<Integer> iconIds = new ArrayList<>();

    public SimpleSelectDialogFragment() {
        setTitleCallback(new TitleCallback<String>() {
            @Override
            public void onGetTitle(TextView titleView, String item, int position) {
                titleView.setText(item);
            }
        });
        setSubtitleCallback(new SubtitleCallback<String>() {
            @Override
            public void onGetSubtitle(TextView subtitleView, String item, int position) {
                if (position < subTitles.size()) {
                    subtitleView.setText(subTitles.get(position));
                }
            }
        });
        setIconCallback(new IconCallback<String>() {
            @Override
            public void onGetIcon(ImageView icon, String item, int position) {
                if (position < iconIds.size()) {
                    icon.setImageResource(iconIds.get(position));
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

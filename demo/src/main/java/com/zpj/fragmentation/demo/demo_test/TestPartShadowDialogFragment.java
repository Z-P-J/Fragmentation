package com.zpj.fragmentation.demo.demo_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.dialog.base.PartShadowDialogFragment;

public class TestPartShadowDialogFragment extends PartShadowDialogFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.test_fragment_dialog;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TestPartShadowDialogFragment", Toast.LENGTH_SHORT).show();
                start(new MainFragment());
                dismiss();
            }
        });
    }


}

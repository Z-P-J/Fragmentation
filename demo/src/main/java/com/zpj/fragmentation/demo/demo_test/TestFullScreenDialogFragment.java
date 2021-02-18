package com.zpj.fragmentation.demo.demo_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.dialog.impl.FullScreenDialogFragment;

public class TestFullScreenDialogFragment extends FullScreenDialogFragment {

    @Override
    protected int getImplLayoutId() {
        return R.layout.test_fragment_bottom_dialog;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
//        getContentView().setBackgroundColor(Color.GRAY);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
//                startWithPop(new MainFragment());
                Toast.makeText(context, "TestFullScreenDialogFragment", Toast.LENGTH_SHORT).show();
//                start(new MainFragment());
                start(new MainFragment());
                dismiss();
            }
        });
    }
}

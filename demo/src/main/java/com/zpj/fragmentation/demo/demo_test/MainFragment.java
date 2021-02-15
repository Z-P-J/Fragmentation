package com.zpj.fragmentation.demo.demo_test;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.zpj.fragmentation.BaseFragment;
import com.zpj.fragmentation.demo.R;
import com.zpj.fragmentation.dialog.impl.AlertDialogFragment;
import com.zpj.fragmentation.dialog.impl.ArrowMenuDialogFragment;
import com.zpj.fragmentation.dialog.impl.AttachListDialogFragment;
import com.zpj.fragmentation.dialog.impl.BottomListDialogFragment;
import com.zpj.fragmentation.dialog.impl.CenterListDialogFragment;
import com.zpj.fragmentation.dialog.impl.CheckDialogFragment;
import com.zpj.fragmentation.dialog.impl.ImageViewerDialogFragment;
import com.zpj.fragmentation.dialog.impl.InputDialogFragment;
import com.zpj.fragmentation.dialog.impl.LoadingDialogFragment;
import com.zpj.fragmentation.dialog.model.OptionMenu;
import com.zpj.recyclerview.EasyViewHolder;
import com.zpj.recyclerview.IEasy;
import com.zpj.widget.checkbox.SmoothCheckBox;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

    private static final List<OptionMenu> optionMenus = new ArrayList<>();

    static {
//        optionMenus.add(new OptionMenu("忽略更新"));
        optionMenus.add(new OptionMenu("详细信息"));
        optionMenus.add(new OptionMenu("分享"));
        optionMenus.add(new OptionMenu("卸载"));
        optionMenus.add(new OptionMenu("打开"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_fragment_main;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

        findViewById(R.id.btn_test_part_shadow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestPartShadowDialogFragment()
                        .setAttachView(v)
                        .show(MainFragment.this);
            }
        });

        findViewById(R.id.btn_test_part_shadow2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestPartShadowDialogFragment()
                        .setAttachView(v)
                        .show(MainFragment.this);
            }
        });

        findViewById(R.id.btn_test_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                start(new TestDialogFragment());
                new AlertDialogFragment()
                        .setTitle("内存使用情况")
                        .setContent("要显示有关应用程序内存使用情况的更多详细信息，您需要在系统设置中获取特殊权限。\n为此，请点击“请求”按钮，然后在应用程序列表中选择Skit并允许访问。")
//                        .setAutoDismiss(false)
                        .setPositiveButton(new AlertDialogFragment.OnButtonClickListener() {
                            @Override
                            public void onClick(AlertDialogFragment fragment) {
                                fragment.start(new TestDialogFragment());
                            }
                        })
                        .show(MainFragment.this);
            }
        });

        findViewById(R.id.btn_test_center_list).setOnClickListener(v -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                list.add(String.valueOf(i));
            }
            new CenterListDialogFragment<String>()
                    .setTitle("CenterListDialogFragment")
                    .setItemLayoutRes(R.layout._dialog_item_text)
                    .setData(list)
                    .setOnBindViewHolderListener(new IEasy.OnBindViewHolderListener<String>() {
                        @Override
                        public void onBindViewHolder(EasyViewHolder holder, List<String> list, int position, List<Object> payloads) {
                            holder.setText(R.id.tv_text, list.get(position));
                        }
                    })
                    .setOnItemClickListener(new IEasy.OnItemClickListener<String>() {
                        @Override
                        public void onClick(EasyViewHolder holder, View view, String data) {
                            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show(MainFragment.this);
        });

//        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Test1", Toast.LENGTH_SHORT).show();
//                start(new MainFragment());
//            }
//        });

        findViewById(R.id.btn_test_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestBottomDialogFragment().show(context);
            }
        });

        findViewById(R.id.btn_test_bottom_list).setOnClickListener(v -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                list.add(String.valueOf(i));
            }
            new BottomListDialogFragment<String>()
                    .setTitle("BottomListDialogFragment")
                    .setData(list)
                    .show(MainFragment.this);
        });

        findViewById(R.id.btn_test_full_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestFullScreenDialogFragment().show(context);
            }
        });

        findViewById(R.id.btn_test_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialogFragment fragment = (LoadingDialogFragment) new LoadingDialogFragment()
                        .setTitle("加载框将在5秒后关闭")
                        .show(MainFragment.this);
                postDelayed(() -> fragment.setTitle("还有2秒"), 3000);
                postDelayed(fragment::dismiss, 2000);
            }
        });

        findViewById(R.id.btn_test_input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InputDialogFragment()
                        .setHint("This is hint!")
//                        .setMinLines(3)
                        .setSingleLine(false)
                        .setMinLines(2)
                        .setMaxLines(5)
                        .setEditText("This is content")
                        .setSelection(2, 7)
                        .setTitle("This is Title")
                        .show(MainFragment.this);
            }
        });

        findViewById(R.id.btn_test_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckDialogFragment()
                        .setChecked(true)
                        .setCheckTitle("This is checkTitle")
                        .setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                                Toast.makeText(context, "isChecked=" + isChecked, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setContent("This is content")
                        .setTitle("This is Title")
                        .show(MainFragment.this);
            }
        });

        findViewById(R.id.btn_test_attach_list_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    list.add(String.valueOf(i));
                }
                new AttachListDialogFragment<String>()
                        .setOnSelectListener(new AttachListDialogFragment.OnSelectListener<String>() {
                            @Override
                            public void onSelect(AttachListDialogFragment<String> fragment, int position, String text) {
                                Toast.makeText(context, "position=" + position + " text=" + text, Toast.LENGTH_SHORT).show();
                                fragment.dismiss();
                            }
                        })
                        .setItems(list)
                        .setAttachView(v)
                        .show(MainFragment.this);
            }
        });

        findViewById(R.id.btn_test_attach_list_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    list.add(String.valueOf(i));
                }
                new AttachListDialogFragment<String>()
                        .setOnSelectListener(new AttachListDialogFragment.OnSelectListener<String>() {
                            @Override
                            public void onSelect(AttachListDialogFragment<String> fragment, int position, String text) {
                                Toast.makeText(context, "position=" + position + " text=" + text, Toast.LENGTH_SHORT).show();
                                fragment.dismiss();
                            }
                        })
                        .setItems(list)
                        .setAttachView(v)
                        .show(MainFragment.this);
            }
        });

        ImageView imageView = findViewById(R.id.iv_test_image_viewer);
        String url = "https://imgo.shouji.com.cn/share/2020/20210211/4871736527_s.jpg";
        String originalUrl = "https://imgo.shouji.com.cn/share/2020/20210211/4871736527.png"; // "https://avatar.shouji.com.cn/20201005/1850813592.jpg"
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageViewerDialogFragment<String>()
                        .setSingleSrcView(imageView, originalUrl)
                        .show(MainFragment.this);
            }
        });
        Glide.with(imageView)
                .load(url)
//                .apply(new RequestOptions().centerCrop()) // .override(Target.SIZE_ORIGINAL)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageDrawable(resource);
                    }
                });




//        ImageView imageView2 = findViewById(R.id.iv_test_image_viewer2);
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<String> imgs = new ArrayList<>();
//                imgs.add(url);
//                new ImageViewerDialogFragment2<String>()
//                        .setSourceImageView(new SourceImageViewGet<String>() {
//                            @Override
//                            public void updateImageView(ImageItemView<String> imageItemView, int pos, boolean isCurrent) {
//                                imageItemView.update(imageView2);
//                            }
//                        })
//                        .setNowIndex(0)
//                        .setImageList(imgs)
//                        .show(context);
//            }
//        });
//        Glide.with(imageView2)
//                .load(url)
//                .apply(new RequestOptions().centerCrop().override(Target.SIZE_ORIGINAL))
//                .into(imageView2);



        findViewById(R.id.btn_test_arrow_menu_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArrowMenuDialogFragment(v);
            }
        });


        findViewById(R.id.btn_test_arrow_menu_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArrowMenuDialogFragment(v);
            }
        });


    }

    private void showArrowMenuDialogFragment(View view) {
        new ArrowMenuDialogFragment()
                .setOptionMenus(optionMenus)
                .setOrientation(LinearLayout.HORIZONTAL)
                .setOnItemClickListener((position, menu) -> {
                    Toast.makeText(context, menu.getTitle(), Toast.LENGTH_SHORT).show();
                })
                .setAttachView(view)
                .show(context);
    }



}

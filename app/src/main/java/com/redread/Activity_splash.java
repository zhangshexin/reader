package com.redread;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutSplashBinding;
import com.redread.kaoshi.Activity_specialList;
import com.redread.utils.GlideUtils;

/**
 * Created by zhangshexin on 2018/8/31.
 *
 *
 * 在没有引导图的情况下显示三行字的渐变效果
 */

public class Activity_splash extends BaseActivity {
    private final String TAG = getClass().getName();
    private LayoutSplashBinding binding;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                 startActivity(Activity_specialList.class);
                 finish2();
                 break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_splash);
        GlideUtils.LoadImageWithLocation(this, R.drawable.spalsh, binding.splashImg);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(1,3300);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(0);
    }

    @Override
    public void onBackPressed() {

    }
}

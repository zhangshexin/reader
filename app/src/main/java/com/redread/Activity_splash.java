package com.redread;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutSplashBinding;
import com.redread.model.entity.DownLoad;
import com.redread.model.gen.DownLoadDao;
import com.redread.net.Api;
import com.redread.net.OkHttpManager;
import com.redread.utils.Constant;
import com.redread.utils.GlideUtils;
import com.redread.utils.IOUtile;
import com.redread.utils.SharePreferenceUtil;

import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangshexin on 2018/8/31.
 *
 *
 * 在没有引导图的情况下显示三行字的渐变效果
 */

public class Activity_splash extends BaseActivity implements View.OnClickListener {
    private final String TAG = getClass().getName();
    private LayoutSplashBinding binding;
    private int plus = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            plus++;
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (plus > 5) {
                        mHandler.removeMessages(1);
                        Intent intent = new Intent(Activity_splash.this, Activity_home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        binding.circleIndicator.setProgress(plus);
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
                case 1:
                    try {
                        GlideUtils.LoadImageWithSize(Activity_splash.this, Constant.picture + "/splash.jpg",801,1241,binding.splashImg);
//                    GlideUtils.glideLoader(Activity_splash.this, Constant.picture + "/splash.jpg", R.drawable.ad, R.drawable.ad, binding.splashImg);
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    //执行显示几行字
                    showText();
                    break;
            }
        }
    };


    private void showText(){
        binding.splashImg.setVisibility(View.GONE);
        binding.splashAlpha.setVisibility(View.VISIBLE);

        //开始动画
        Animation alpha1= AnimationUtils.loadAnimation(this,R.anim.gradualchange);
        binding.alphaText1.setVisibility(View.VISIBLE);
        binding.alphaText1.startAnimation(alpha1);

        Animation alpha2= AnimationUtils.loadAnimation(this,R.anim.gradualchange);
        alpha2.setStartOffset(1000);
        binding.alphaText2.setVisibility(View.VISIBLE);
        binding.alphaText2.startAnimation(alpha2);

        Animation alpha3= AnimationUtils.loadAnimation(this,R.anim.gradualchange);
        alpha3.setStartOffset(2000);
        binding.alphaText3.setVisibility(View.VISIBLE);
        binding.alphaText3.startAnimation(alpha3);

        Animation alpha4= AnimationUtils.loadAnimation(this,R.anim.gradualchange);
        alpha4.setStartOffset(3000);
        binding.alphaText4.setVisibility(View.VISIBLE);
        binding.alphaText4.startAnimation(alpha4);

        mHandler.sendEmptyMessageDelayed(0, 4500);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_splash);
        binding.splashJump.setOnClickListener(this);
        binding.circleIndicator.setOnClickListener(this);
        //修改为从网格或不显示,显示那几行字
//        File splashFile = new File(Constant.picture + "/splash.jpg");
//        if (splashFile.exists()) {
////            GlideUtils.glideLoader(Activity_splash.this, Constant.picture + "/splash.jpg", R.drawable.ad, R.drawable.ad, binding.splashImg);
//            GlideUtils.LoadImageWithSize(this, Constant.picture + "/splash.jpg", 800,1240, binding.splashImg);
//        } else {
//            GlideUtils.LoadImageWithLocation(this, R.drawable.ad, binding.splashImg);
//        }
        initDBData();
        loadSplashPic();
    }

    /**
     * 下载启动图
     */
    private void loadSplashPic() {
//        Request request = new Request.Builder().url(Api.downUrl + "splash.jpg").build();
//        Call mCall = OkHttpManager.getInstance(this).getmOkHttpClient().newCall(request);
//        mCall.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(2);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    File picDir = new File(Constant.picture);
//                    if (!picDir.isDirectory())
//                        picDir.mkdirs();
//                    File splashFile = new File(picDir, "splash.jpg");
//                    InputStream ins = response.body().byteStream();
//                    if(splashFile.exists() && ins.available() != splashFile.length()){
//                        splashFile.delete();
//                    }
//                    if (!splashFile.exists()) {
//                        FileOutputStream fos = new FileOutputStream(splashFile);
//                        byte[] buf = new byte[1024];
//                        int length;
//                        while ((length = ins.read(buf)) != -1) {
//                            fos.write(buf, 0, length);
//                        }
//                        fos.flush();
//                        ins.close();
//                        fos.close();
//                    }
//                    mHandler.sendEmptyMessageDelayed(1, 1000);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }


    /**
     * 初始化一些必要数据，写入一本书
     */
    private synchronized void initDBData() {
        DownLoadDao dao = MyApplication.getInstances().getDaoSession().getDownLoadDao();
        //把所有等待下载和下载中的更新为暂停
        List<DownLoad> searchResult = dao.queryBuilder().where(new WhereCondition.StringCondition("status =" + Constant.DOWN_STATUS_WAIT + " or status = " + Constant.DOWN_STATUS_ING)).list();
        for (int i = 0; i < searchResult.size(); i++) {
            searchResult.get(i).setStatus(Constant.DOWN_STATUS_PAUS);
        }
        dao.updateInTx(searchResult);

        Boolean isFirstEntry = (Boolean) SharePreferenceUtil.getSimpleData(this, Constant.KEY_BOOLEAN_FIRST_USE, true);
        if (!isFirstEntry)
            return;
        //把书放到sd卡下
        IOUtile.putAssetsToSDCard(this, "defaultbook.txt", Constant.bookPath);
        DownLoad downLoad = new DownLoad();
        downLoad.setStatus(Constant.DOWN_STATUS_SUCCESS);
        downLoad.setBookName("元尊");
        downLoad.setBookDir(Constant.bookPath + "/defaultbook.txt");
        downLoad.setUpDate(new Date(System.currentTimeMillis()));
        downLoad.setBookType(Constant.BOOK_TYPE_TXT);
        dao.insert(downLoad);
        SharePreferenceUtil.saveSimpleData(this, Constant.KEY_BOOLEAN_FIRST_USE, false);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(0);
    }

    @Override
    public void onBackPressed() {
        mHandler.removeMessages(0);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_jump:
            case R.id.circleIndicator:
                plus = 10;
                mHandler.sendEmptyMessage(0);
                break;
        }
    }
}

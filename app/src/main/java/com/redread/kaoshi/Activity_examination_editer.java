package com.redread.kaoshi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONObject;
import com.redread.R;
import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutExaminationEditerBinding;
import com.redread.kaoshi.bean.Questions;
import com.redread.net.Api;
import com.redread.net.OkHttpManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangshexin on 2019/4/22.
 * 考题编辑页面
 */

public class Activity_examination_editer extends BaseActivity {

    private LayoutExaminationEditerBinding binding;
    private Questions questions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_examination_editer);
        questions = (Questions) getIntent().getSerializableExtra("examination");

        binding.setAct(this);
        binding.setQuestion(questions);


        binding.examinationEdiType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.examinationTypeSelect:
                        questions.setType(1);
                        break;
                    case R.id.examinationTypeAdd:
                        questions.setType(2);
                        break;
                }
            }
        });

        binding.title.titleTitle.setText(getTitle());
        binding.title.titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish2();
            }
        });
        binding.title.titleRight.setText("保存");
        binding.title.titleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }


    private boolean dataIsLoading = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dataIsLoading = false;
            switch (msg.what) {
                case 0:
                    showToast("失败");
                    break;
                case 1:
                case 2:
                    showToast("成功");
                    if (msg.what == 1)
                        finish2();
                    break;
            }
        }
    };


    /**
     * 保存或更新
     */
    private void save() {
        if (dataIsLoading)
            return;
        dataIsLoading = false;

        String jsonParams = JSONObject.toJSONString(questions);

        Request request = Api.saveExaminationPost(this, jsonParams);

        Call mCall = OkHttpManager.getInstance(this).getmOkHttpClient().newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    Log.e(TAG, "save response: " + json);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    if (jsonObject.getInteger("code") == 200)
                        if (questions.getId() > 0)
                            mHandler.sendEmptyMessage(2);
                        else
                            finish2();
                    else
                        mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    /**
     * 删除去考题
     */
    public void deleteExamination() {
        if (dataIsLoading)
            return;
        dataIsLoading = false;

        String params = "ids=" + questions.getId();

        Request request = Api.deleteExaminationByIdsPost(this, params);

        Call mCall = OkHttpManager.getInstance(this).getmOkHttpClient().newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    Log.e(TAG, "deleteExamination response: " + json);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    if (jsonObject.getInteger("code") == 200)
                        mHandler.sendEmptyMessage(1);
                    else
                        mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

}

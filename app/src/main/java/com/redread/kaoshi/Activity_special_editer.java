package com.redread.kaoshi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.redread.R;
import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutSpecialEditerBinding;
import com.redread.kaoshi.adapter.Adapter_examination;
import com.redread.kaoshi.bean.Questions;
import com.redread.kaoshi.bean.Special;
import com.redread.net.Api;
import com.redread.net.OkHttpManager;
import com.redread.utils.SystemUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangshexin on 2019/4/22.
 * 专题编辑页
 */

public class Activity_special_editer extends BaseActivity implements View.OnClickListener {
    private String TAG = getClass().getName();
    private LayoutSpecialEditerBinding binding;
    private Special special;
    private Adapter_examination adapter_examination;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_special_editer);
        if (getIntent().hasExtra("special"))
            special = (Special) getIntent().getSerializableExtra("special");
        else
            special = new Special();
        binding.setSpecial(special);
        binding.setAct(this);

        binding.title.titleLeft.setOnClickListener(this);
        binding.title.titleRight.setOnClickListener(this);
        binding.title.titleRight.setText("保存");
        binding.title.titleTitle.setText(getTitle());


        adapter_examination = new Adapter_examination(this, SystemUtil.isAdamin(), questions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.speicalQuestionList.setLayoutManager(layoutManager);
        binding.speicalQuestionList.setAdapter(adapter_examination);

        loadExaminationList(special.getType() ? special.getQuestions() : null, special.getId());
    }

    public void addNewQuestion() {
        startActivity(Activity_examination_editer.class);
    }

    public void deleteSpecial(int id) {
        String params = "ids=" + id;
        Request request = Api.deleteSpecialByIdsPost(this, params);
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
                    Log.e(TAG, "onResponse3: " + json);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    if (jsonObject.getInteger("code") == 200) {
                        mHandler.sendEmptyMessage(1);
                    } else
                        mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    private List<Questions> questions = new ArrayList<>();

    /**
     * 如果是人工选题要加载选了的题，如果是随机的则显示所有题
     *
     * @param exaIds
     * @param specialId
     */
    private void loadExaminationList(String exaIds, int specialId) {
        Request request;
        if (exaIds != null)
            request = Api.examinationByIdsGet(this, exaIds);
        else
            request = Api.specialExaminationGet(this, specialId);
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
                    Log.e(TAG, "onResponse1: " + json);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    if (jsonObject.getInteger("code") == 200) {
                        //展示考题
                        questions.clear();
                        questions .addAll(JSONObject.parseArray(jsonObject.getString("result"), Questions.class));
                        mHandler.sendEmptyMessage(1);
                    } else
                        mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish2();
                break;
            case R.id.title_right:
                save();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showToast("失败");
                    break;
                case 1:
                    showToast("成功");
                    adapter_examination.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void save() {
        Map<String, Object> params = (Map<String, Object>) JSONObject.parse(JSONObject.toJSONString(special));
        params.put("type", (Boolean) params.get("type") ? 1 : 0);
        Request request = Api.saveSpecialPost(this, JSONObject.toJSONString(params));
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
                    Log.e(TAG, "onResponse2: " + json);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    if (jsonObject.getInteger("code") == 200) {
                        if (special.getId() == 0)
                            finish2();
                    } else
                        mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }
}

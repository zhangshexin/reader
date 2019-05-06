package com.redread.kaoshi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.redread.MyApplication;
import com.redread.R;
import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutExaminationBinding;
import com.redread.kaoshi.adapter.Adapter_fragmentExamination;
import com.redread.kaoshi.bean.Grade;
import com.redread.kaoshi.bean.Questions;
import com.redread.kaoshi.bean.Special;
import com.redread.kaoshi.fragment.Fragment_examination;
import com.redread.model.entity.User;
import com.redread.net.Api;
import com.redread.net.OkHttpManager;
import com.redread.utils.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangshexin on 2019/4/22.
 * 考试页
 */

public class Activity_examination extends BaseActivity {

    private LayoutExaminationBinding binding;
    private Special special;
    private Grade grade;
    private long examinationTime = 0;//考试时长,单位秒

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_examination);
        binding.setAct(this);
        binding.setCurrentPage(STAGE_PRE);

        binding.title.titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish2();
            }
        });


        binding.title.titleTitle.setText(getTitle());

        special = (Special) getIntent().getSerializableExtra("special");

        examinationTime = Long.parseLong(special.getTestTime());

        binding.setSpecial(special);
        //取当前的成绩，排名，前10名
        getHistoryGradeDataFromNet(SystemUtil.getUser().getId(), special.getId(), 10);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dataIsLoading = false;
            switch (msg.what) {
                case 0:
                    showToast("失败");
                    break;
                case 1:
                    binding.setGrade(grade);
                    if(currentStage==STAGE_PRE){
                        //处理一下总排总
                        List<Grade> topN = grade.getTopN();
                        String top = "";
                        for (int i = 0; i < topN.size(); i++) {
                            top = top + "第" + (i + 1) + "名分数为【" + topN.get(i).getGrade() + "】手机号为【" + topN.get(i).getPhoneNum() + "】\n";
                        }
                        if (TextUtils.isEmpty(top))
                            top = "暂时没有排名";
                        binding.setTopTen(top);
                        //根据管理员的配置来显示考题内容,如果是人工选题，但确没有给配题则直接随机返回考题
                        if (special.getType()) {
                            getQuestionsFromNet(special.getQuestions(), special.getId());
                        } else {
                            getQuestionsFromNet(null, special.getId());
                        }
                    }else if(currentStage==STAGE_FINISH){
                        //无须操作
                    }
                    binding.notifyChange();
                    break;
                case 2:
                    //提交完成绩切到结果页
                    currentStage = STAGE_FINISH;
                    binding.setCurrentPage(STAGE_FINISH);
                    binding.notifyChange();
                    getHistoryGradeDataFromNet(SystemUtil.getUser().getId(), special.getId(), 10);
                    break;
            }
        }
    };
    /**
     * 三个阶段，1=pre，2=ing，3=finish
     */
    private final int STAGE_PRE = 1;
    private final int STAGE_ING = 2;
    private final int STAGE_FINISH = 3;

    private int currentStage = STAGE_PRE;

    private boolean dataIsLoading = false;

    private List<Questions> questionsList;

    /**
     * 返回指定考题id内容或着是指定的专题id随机考题
     *
     * @param ids
     * @param specialId
     */
    private void getQuestionsFromNet(String ids, int specialId) {
        if (dataIsLoading)
            return;
        dataIsLoading = true;
        Request request;
        if (!TextUtils.isEmpty(ids))
            request = Api.examinationByIdsGet(this, ids);
        else
            request = Api.examinationRandomIdsGet(this, specialId, special.getCount());
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
                    Log.e(TAG, "loadQuestionsFromNet onResponse: " + json);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    if (jsonObject.getInteger("code") == 200) {
                        questionsList = JSONObject.parseArray(jsonObject.getString("result"), Questions.class);
                    } else {
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }

            }
        });
    }

    /**
     * 获取用户指定专题的历史成绩
     *
     * @param userId
     * @param specialId
     */
    private void getHistoryGradeDataFromNet(long userId, int specialId, int top) {
        if (dataIsLoading)
            return;
        dataIsLoading = true;
        Request request = Api.gradeAndRankGet(this, userId, specialId, top);
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
                    Log.e(TAG, "getHistoryGradeDataFromNet onResponse: " + json);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    if (jsonObject.getInteger("code") == 200) {
                        grade = JSONObject.parseObject(jsonObject.getString("result"), Grade.class);
                        mHandler.sendEmptyMessage(1);
                    } else {
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }

            }
        });
    }

    /**
     * 开始考试
     */
    public void startExamination() {
        if (dataIsLoading) {
            showToast("数据获取中请稍后！");
            return;
        }
        if (questionsList == null || questionsList.size() == 0) {
            showToast("没有可用的考题，请联系管理员处理！");
            return;
        }
        currentStage = STAGE_ING;
        binding.setCurrentPage(STAGE_ING);
        binding.notifyChange();

        //考试时间开始倒计
        timeHandler.sendEmptyMessage(1);
        //渲染考题
        Fragment_examination fragment1 = new Fragment_examination();
        Fragment_examination fragment2 = new Fragment_examination();
        Fragment_examination fragment3 = new Fragment_examination();
        examinations.add(fragment1);
        examinations.add(fragment2);
        examinations.add(fragment3);
        adapter_fragmentExamination = new Adapter_fragmentExamination(getSupportFragmentManager(), examinations, questionsList);
        binding.examinationContainer.setAdapter(adapter_fragmentExamination);
    }
    private List<Fragment_examination> examinations = new ArrayList<>();
    private Adapter_fragmentExamination adapter_fragmentExamination;

    /**
     * 下一题或着是交卷
     */
    public void nextExamination(int position) {
        if (position >= questionsList.size() - 1) {
            //该提交考卷了
            submitGrade();
        } else{

            //标记为已作
            questionsList.get(position).setDid(true);
            //判分
            int realPosition=position%3;
            questionsList.get(position).setRightOrWrong(examinations.get(realPosition).estimateGrade());
            //下一题
            binding.examinationContainer.setCurrentItem(position+1, true);
        }
    }

    /**
     * 提交成绩
     */
    private synchronized void submitGrade(){
        if(dataIsLoading)
            return;
        dataIsLoading=true;
        User user=SystemUtil.getUser();
        int grade=0;
        for (Questions qu:questionsList
             ) {
            grade+=qu.getRightOrWrong();
        }
        Request request=Api.updateGradeByIdPost(this,user.getId(),grade);
        Call mCall=OkHttpManager.getInstance(this).getmOkHttpClient().newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json=response.body().string();
                    Log.e(TAG, "submitGrade onResponse: "+json);

                 JSONObject jsonObject=   JSONObject.parseObject(json);
                 if(jsonObject.getInteger("code")==200){
                     mHandler.sendEmptyMessage(2);
                 }else{
                     mHandler.sendEmptyMessage(0);
                 }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }




    //用于实现倒计时
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (examinationTime <= 0) {
                //考试结束，需提交
                completeExamination();
                return;
            }
            examinationTime -= 1;
            //秒转分钟
            long minute = examinationTime / 60;
            long second = examinationTime % 60;
            String time = minute + ":" + second;
            binding.setRefreshTime(time);
            binding.notifyChange();
            timeHandler.sendEmptyMessageDelayed(1, 1000);
        }
    };

    //考试结束
    private void completeExamination() {

    }

    private void startTime() {
        timeHandler.sendEmptyMessageDelayed(1, 1000);
    }

    ;

    private void resumeTime() {
        try {
            timeHandler.removeMessages(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ;

    private void pauseTime() {
        timeHandler.sendEmptyMessageDelayed(1, 1000);
    }

    ;

    private void stopTime() {
        try {
            timeHandler.removeMessages(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ;

    @Override
    protected void onResume() {
        super.onResume();
        //如果是在考试过程中需要继续计时
        if (currentStage == STAGE_ING)
            resumeTime();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //如果是在考试过程中需要暂停计时
        if (currentStage == STAGE_ING)
            pauseTime();
    }

    /**
     * 完成本次考试
     */
    public void finishExamination() {
        stopTime();
        finish2();
    }
}

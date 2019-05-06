package com.redread.kaoshi.fragment;

import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.redread.R;
import com.redread.base.BaseFragment;
import com.redread.databinding.FragmentExaminationBinding;
import com.redread.kaoshi.Activity_examination;
import com.redread.kaoshi.bean.Questions;
import com.redread.widget.answer_edittext.ReplaceSpan;
import com.redread.widget.answer_edittext.SpanController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangshexin on 2019/4/30.
 */

public class Fragment_examination extends BaseFragment implements ReplaceSpan.OnClick {
    private FragmentExaminationBinding binding;
    private int position;
    private String btnTitle;//按钮的标题

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private Questions questions;

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_examination,container,false);
        mSpc = new SpanController();
        return binding.getRoot();
    }

    private String mStr;
    private SpanController mSpc;//Span的控制类

    private void initData(String question,String answer) {
        mStr = question;
        mSpc.makeData(this,binding.fragmentExaminationQuestion,mStr,answer);

        binding.fragmentExaminationQuesEdi.addTextChangedListener(mWatcher);
        binding.fragmentExaminationQuesEdi.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSpc.mWidthStr.length())});
    }
    String asnwer="aswk";
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&questions!=null){
            if(questions.getType()==2){//填空填要处理
                String json=questions.getAnswer();
                JSONObject jsonObject=JSONObject.parseObject(json);
                asnwer=jsonObject.getString("right");
            }
            initData(questions.getQuestion(),asnwer);
            //渲染
            try {
                String question=questions.getQuestion();
                JSONObject answersObject=  JSONObject.parseObject(questions.getAnswer());
                String answer=answersObject.getString("right");//正确答案
                String allAnswer=answersObject.getString("all");//选择题时的选项
                String[] allAnswerArray=null;//选项数组
                if(!TextUtils.isEmpty(allAnswer)){
                    allAnswerArray=allAnswer.split(",");
                }
                boolean isChoice=questions.getType()==1;


                //开始赋值
                binding.setQuestion(question);
                binding.setIsChoice(isChoice);
                binding.setAnswers(answer);
                if (!TextUtils.isEmpty(allAnswer) && allAnswerArray.length >= 1) {
                    binding.setA(allAnswerArray[0]);
                    if (allAnswerArray.length >= 2)
                        binding.setA(allAnswerArray[1]);
                    if (allAnswerArray.length >= 3)
                        binding.setA(allAnswerArray[2]);
                    if (allAnswerArray.length >= 4)
                        binding.setA(allAnswerArray[3]);
                }
                binding.setFragment((Activity_examination) getActivity());
                binding.setPositon(position);
                binding.setBtnTitle(btnTitle);
                binding.notifyChange();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"题解析出错请联系管理员",Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 判分
     */
    public int estimateGrade(){
        boolean result=true;
        //选择题
        if(questions.getType()==1){
            JSONObject answersObject=  JSONObject.parseObject(questions.getAnswer());
            String[] answers=answersObject.getString("right").split(",");//正确答案
            Map<String,String> answerMap=new HashMap<>();
            for (int i=0;i<answers.length;i++){
                answerMap.put(answers[i],"");
            }

            int time=0;
            CharSequence  a=binding.fragmentExaminationChoiceA.getText();
            String aStr=null;
            if(a!=null&&binding.fragmentExaminationChoiceA.isChecked()) {
                aStr = a.toString();
                result=result&&answerMap.containsKey(aStr);
                time++;
            }


            CharSequence  b=binding.fragmentExaminationChoiceB.getText();
            String bStr=null;
            if(b!=null&&binding.fragmentExaminationChoiceB.isChecked()) {
                bStr = b.toString();
                result=result&&answerMap.containsKey(bStr);
                time++;
            }

            CharSequence c=binding.fragmentExaminationChoiceC.getText();
            String cStr=null;
            if(c!=null&&binding.fragmentExaminationChoiceC.isChecked()){
                cStr=c.toString();
                result=result&&answerMap.containsKey(cStr);
                time++;
            }

            CharSequence  d=binding.fragmentExaminationChoiceD.getText();
            String dStr=null;
            if(d!=null&&binding.fragmentExaminationChoiceD.isChecked()) {
                dStr = d.toString();
                result=result&&answerMap.containsKey(dStr);
                time++;
            }

            //没选或着是错选都得零分
            if((aStr==null&&bStr==null&&cStr==null&&dStr==null)||time!=answers.length) return 0;
            else if(result) return 1;
            else return 0;

        }else{
            //填空
           String allAns= mSpc.getAllAnswer();
           if(allAns==null) return 0;
           String[] allAnsArray=allAns.split(",");
           String[] rightAns=asnwer.split(",");
           if(allAnsArray.length!=rightAns.length) return 0;
           int hitTime=0;
           for (String a:allAnsArray){
               for (String r:rightAns){
                   if(a.trim().equals(r.trim()))
                       hitTime++;
               }
           }
           if(hitTime==rightAns.length) return 1;return 0;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void OnClick(TextView v, int id, ReplaceSpan span) {
        if(questions.getType()==1) return;//选择题不用管这里
        mSpc.setData(binding.fragmentExaminationQuesEdi.getText().toString(),null,mSpc.mOldSpan);
        mSpc.mOldSpan = id;
        //如果当前span身上有值，先赋值给et身上
        binding.fragmentExaminationQuesEdi.setText(TextUtils.isEmpty(span.mText)?"":span.mText);
        //通过rf计算出et当前应该显示的位置
        RectF rf = mSpc.drawSpanRect(binding.fragmentExaminationQuestion,span);
        //设置EditText填空题中的相对位置
        mSpc.setEtXY(binding.fragmentExaminationQuestion,binding.fragmentExaminationQuesEdi,rf);
    }

    //输入填空的监听
    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try{
                mSpc.setData(s.toString(),null,mSpc.mOldSpan);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}

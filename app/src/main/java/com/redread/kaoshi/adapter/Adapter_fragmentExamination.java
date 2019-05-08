package com.redread.kaoshi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.redread.kaoshi.Activity_examination;
import com.redread.kaoshi.bean.Questions;
import com.redread.kaoshi.fragment.Fragment_examination;

import java.util.List;

/**
 * Created by zhangshexin on 2019/5/5.
 */

public class Adapter_fragmentExamination extends FragmentPagerAdapter {

    private List<Fragment_examination> fragment_examinations;
    private List<Questions> questions;
    public Adapter_fragmentExamination(FragmentManager fm, List<Fragment_examination> fragment_examinations, List<Questions> questions) {
        super(fm);
        this.fragment_examinations = fragment_examinations;
        this.questions=questions;
    }
    @Override
    public Fragment_examination getItem(int position) {
        return fragment_examinations.get(position);
    }

    @Override
    public void doOther(int position, Fragment fragment) {
        super.doOther(position, fragment);
        Fragment_examination examination=(Fragment_examination)fragment;
        examination.setPosition(position);
        examination.setBtnTitle(position==questions.size()-1?"完成考试":"想好了提交并进入下一题");
        examination.setQuestions(questions.get(position));
    }

    @Override
    public int getCount() {
        return questions.size();
    }
}

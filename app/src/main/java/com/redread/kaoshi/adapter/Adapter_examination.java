package com.redread.kaoshi.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redread.R;
import com.redread.base.BaseRecycelAdapter;
import com.redread.base.BaseViewHolder;
import com.redread.databinding.ItemExaminationBinding;
import com.redread.databinding.ItemSpecialBinding;
import com.redread.kaoshi.Activity_examination_editer;
import com.redread.kaoshi.Activity_special_editer;
import com.redread.kaoshi.bean.Questions;
import com.redread.kaoshi.bean.Special;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhangshexin on 2019/4/25.
 * 考题适配器
 */

public class Adapter_examination extends BaseRecycelAdapter<BaseViewHolder> {
    private boolean isAdmin;
    private List<Questions> questions = Collections.emptyList();
    private Context mContext;

    public Adapter_examination(Context context, boolean isAdmin, List<Questions> questions) {
        super(context);
        this.mContext=context;
        this.isAdmin = isAdmin;
        this.questions = questions;
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemExaminationBinding itemExaminationBinding = DataBindingUtil.inflate(inflater, R.layout.item_examination, parent, false);
        return new BaseViewHolder(itemExaminationBinding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemExaminationBinding itemExaminationBinding =  (ItemExaminationBinding) holder.getBinding();
        itemExaminationBinding.setAdapter(this);
        itemExaminationBinding.setQuestion(questions.get(position));
        Questions exiamination = questions.get(position);
        TextView title = itemExaminationBinding.examintionTitle;
        title.setText(exiamination.getQuestion());

        if (!isAdmin) {
            itemExaminationBinding.examinationEdiBtn.setVisibility(View.GONE);
        }

    }

    public void editQuestion(Questions position) {
        //跳转到编页
        Intent goEditSpecial=new Intent(mContext, Activity_examination_editer.class);
        goEditSpecial.putExtra("examination",position);
        mContext.startActivity(goEditSpecial);
    }
}

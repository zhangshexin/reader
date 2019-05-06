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
import com.redread.databinding.ItemSpecialBinding;
import com.redread.kaoshi.Activity_examination;
import com.redread.kaoshi.Activity_special_editer;
import com.redread.kaoshi.bean.Special;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhangshexin on 2019/4/25.
 * 专题适配器
 */

public class Adapter_special extends BaseRecycelAdapter<BaseViewHolder> {
    private boolean isAdmin;
    private List<Special> specials = Collections.emptyList();
    private Context mContext;

    public Adapter_special(Context context, boolean isAdmin, List<Special> specials) {
        super(context);
        this.mContext = context;
        this.isAdmin = isAdmin;
        this.specials = specials;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public int getItemCount() {
        return specials.size();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemSpecialBinding itemSpecialBinding = DataBindingUtil.inflate(inflater, R.layout.item_special, parent, false);
        return new BaseViewHolder(itemSpecialBinding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemSpecialBinding itemSpecialBinding = (ItemSpecialBinding) holder.getBinding();
        itemSpecialBinding.setAdapter(this);
        itemSpecialBinding.setPosition(position);
        Special special = specials.get(position);
        TextView title = itemSpecialBinding.specialItemTitle;
        title.setText(special.getSpecialName());
        TextView des = itemSpecialBinding.specialItemDes;
        des.setText(special.getSpecialDes());
        if (!isAdmin) {
            itemSpecialBinding.specialItemEdiBtn.setVisibility(View.GONE);
        } else {
            itemSpecialBinding.specialItemEdiBtn.setVisibility(View.VISIBLE);
        }

    }

    public void editSpecial(int position) {
        //跳转到编页
        Intent goEditSpecial = new Intent(mContext, Activity_special_editer.class);
        goEditSpecial.putExtra("special", specials.get(position));
        mContext.startActivity(goEditSpecial);
    }

    public void startExamination(int position) {
        if (!isAdmin) {//非管理员去考试
            Intent goEditSpecial = new Intent(mContext, Activity_examination.class);
            goEditSpecial.putExtra("special", specials.get(position));
            mContext.startActivity(goEditSpecial);
        }
    }
}

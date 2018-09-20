package com.redread.libary.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.ViewGroup;

import com.redread.R;
import com.redread.base.BaseRecycelAdapter;
import com.redread.base.BaseViewHolder;
import com.redread.databinding.LayoutLibarymodelCellBinding;
import com.redread.libary.Activity_bookDetail;
import com.redread.libary.Activity_modeDetaillList;
import com.redread.net.netbean.NetBeanLibaryModel;

import java.util.List;

/**
 * Created by zhangshexin on 2018/9/20.
 */

public class Adapter_libaryModel extends BaseRecycelAdapter<BaseViewHolder> {
    private String TAG=getClass().getName();
    private List<NetBeanLibaryModel> models;

    public Adapter_libaryModel(Context context, List<NetBeanLibaryModel> models) {
        super(context);
        this.models = models;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutLibarymodelCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_libarymodel_cell, parent, false);
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        NetBeanLibaryModel model = models.get(0);
        LayoutLibarymodelCellBinding binding = (LayoutLibarymodelCellBinding) holder.getBinding();
        binding.setAdapter(this);
        binding.setPosition(0);
    }

    /**
     * 去图书详情页
     * @param position
     */
    public void goBookDetail(int position,int bookP){
        Log.e(TAG, bookP+"--------goBookDetail: =================="+position );
        Intent intent=new Intent(mContext,Activity_bookDetail.class);
        mContext.startActivity(intent);
    }

    /**
     * 去模块详情页
     * @param position
     */
    public void goModelDetailList(int position){
        Intent intent=new Intent(mContext,Activity_modeDetaillList.class);
        mContext.startActivity(intent);
    }
}

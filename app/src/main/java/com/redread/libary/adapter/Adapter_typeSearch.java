package com.redread.libary.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import com.redread.R;
import com.redread.base.BaseRecycelAdapter;
import com.redread.base.BaseViewHolder;
import com.redread.databinding.LayoutTypesearchCellBinding;


/**
 * Created by zhangshexin on 2018/9/21.
 */

public class Adapter_typeSearch extends BaseRecycelAdapter<BaseViewHolder> {
    public Adapter_typeSearch(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return 7;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutTypesearchCellBinding binding= DataBindingUtil.inflate(inflater, R.layout.layout_typesearch_cell,parent,false);
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}

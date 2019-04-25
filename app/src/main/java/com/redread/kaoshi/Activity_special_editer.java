package com.redread.kaoshi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.redread.R;
import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutSpecialEditerBinding;

/**
 * Created by zhangshexin on 2019/4/22.
 * 专题编辑页
 */

public class Activity_special_editer extends BaseActivity{
    private LayoutSpecialEditerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_special_editer);
    }
}

package com.redread.kaoshi;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.redread.MyApplication;
import com.redread.R;
import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutHomeBinding;
import com.redread.login.Activity_Login;
import com.redread.model.entity.User;
import com.redread.model.gen.UserDao;

import java.util.List;

/**
 * Created by zhangshexin on 2019/4/22.
 * <p>
 * <p>
 * 专题列表
 */

public class Activity_specialList extends BaseActivity {
    private LayoutHomeBinding binding;
    private UserDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_home);
        dao = MyApplication.getInstances().getDaoSession().getUserDao();
        initView();
    }


    private void initView() {
        binding.title.titleLeft.setVisibility(View.INVISIBLE);
        binding.userExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清理用户信息
                dao.deleteAll();
                checkLogin();
            }
        });

    }


    /**
     * 打开/关闭左侧滑
     */
    public void toggleSlidNav() {
        if (binding.homeDrawerLayout.isDrawerOpen(Gravity.START))
            binding.homeDrawerLayout.closeDrawer(Gravity.START);
        else
            binding.homeDrawerLayout.openDrawer(Gravity.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是否已登录，没登录跳转登录
        checkLogin();
    }

    //判断是否已登录，没登录跳转登录
    private void checkLogin() {


        List<User> users = dao.loadAll();

        if (users == null || users.size() == 0) {
            startActivity(Activity_Login.class);
            return;
        }
        binding.userPhone.setText(users.get(0).getPhone());

        boolean isAdmin=users.get(0).getRole().equals("admin");
        binding.userRole.setText(isAdmin? "管理员" : "普通用户");

        binding.title.titleTitle.setText(isAdmin ? "管理考试" : "考试");

        //展示管理员操作按钮
        if(isAdmin){

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        // 为Intent设置Action、Category属性
        intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
        intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
        startActivity(intent);
    }
}

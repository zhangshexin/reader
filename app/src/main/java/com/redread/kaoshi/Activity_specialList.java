package com.redread.kaoshi;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.redread.MyApplication;
import com.redread.R;
import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutHomeBinding;
import com.redread.kaoshi.adapter.Adapter_special;
import com.redread.kaoshi.bean.Special;
import com.redread.login.Activity_Login;
import com.redread.model.entity.User;
import com.redread.model.gen.UserDao;
import com.redread.net.Api;
import com.redread.net.OkHttpManager;
import com.redread.utils.SystemUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangshexin on 2019/4/22.
 * <p>
 * <p>
 * 专题列表
 */

public class Activity_specialList extends BaseActivity {
    private String TAG = getClass().getName();
    private LayoutHomeBinding binding;
    private UserDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_home);
        dao = MyApplication.getInstances().getDaoSession().getUserDao();
        initView();
    }

    private Adapter_special adapter_special;
    private List<Special> specialList = new ArrayList();

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

        adapter_special = new Adapter_special(this, SystemUtil.isAdamin(), specialList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.specialList.setLayoutManager(layoutManager);
        binding.specialList.setAdapter(adapter_special);
        binding.loadMore.loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(true);
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

        boolean isAdmin = users.get(0).getRole().equals("admin");
        binding.userRole.setText(isAdmin ? "管理员" : "普通用户");

        binding.title.titleTitle.setText(isAdmin ? "管理考试" : "考试");

        //展示管理员操作按钮
        if (isAdmin) {
            //右上角"新增"按钮
            binding.title.titleRight.setText("新增专题");
            binding.title.titleRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Activity_special_editer.class);
                }
            });
        } else {
            binding.title.titleRight.setVisibility(View.INVISIBLE);
        }
        //加载数据
        loadData(false);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    binding.loadMore.getRoot().setVisibility(View.GONE);
                    break;
                case 1:
                    adapter_special.notifyDataSetChanged();
                    break;
            }
        }
    };
    private int currentPageNum = 1;
    private final int pageSize = 30;
    private int status = 1;
    private boolean isLoadingData = false;

    private synchronized void loadData(boolean isLoadMore) {
        if (isLoadingData)
            return;
        isLoadingData = true;
        if(!isLoadMore)
        {
            specialList.clear();
            currentPageNum=1;
        }
        if (currentPageNum > 1) {
            currentPageNum++;
        }
        Request mRequest = Api.specialListGet(this, currentPageNum, pageSize, status);
        Call mCall = OkHttpManager.getInstance(this).getmOkHttpClient().newCall(mRequest);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (currentPageNum > 1) {
                    currentPageNum--;
                }
                isLoadingData = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e(TAG, "onResponse: " + json);
                JSONObject jsonObject = JSONObject.parseObject(json);
                if (jsonObject.getInteger("code") == 200) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    List<Special> specials = JSONObject.parseArray(result.getString("list"), Special.class);
                    if (specials.size() < pageSize) {
                        mHandler.sendEmptyMessage(0);
                    }
                    specialList.addAll(specials);
                    mHandler.sendEmptyMessage(1);
                } else {
                    if (currentPageNum > 1) {
                        currentPageNum--;
                    }
                }
                isLoadingData = false;
            }
        });
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

package com.redread.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.redread.MyApplication;
import com.redread.R;
import com.redread.base.BaseActivity;
import com.redread.databinding.LayoutLoginGeneralBinding;
import com.redread.model.entity.User;
import com.redread.model.gen.UserDao;
import com.redread.net.Api;
import com.redread.net.OkHttpManager;
import com.redread.rxbus.RxBus;
import com.redread.rxbus.RxSubscriptions;
import com.redread.rxbus.bean.FinishRX;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by zhangshexin on 2018/9/14.
 * <p>
 * 验证码登录
 */

public class Activity_Login extends BaseActivity implements View.OnClickListener {

    private String TAG = getClass().getName();

    private LayoutLoginGeneralBinding binding;
    private final int what_code_fail = 0;//验证码失败
    private final int what_code_success = 1;//验证码成功


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ableBtn(true);
            switch (msg.what) {
                case what_code_fail:
                    showToast(loginMsg);
                    break;
                case what_code_success:
                    finish2();
                    break;
            }
        }
    };

    private UserDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_login_general);
        dao = MyApplication.getInstances().getDaoSession().getUserDao();
        initView();
    }

    private void initView() {
        binding.loginGeneralInclude.titleLeft.setVisibility(View.INVISIBLE);
        binding.loginGeneralInclude.titleTitle.setText(getTitle().toString());
        binding.loginRegister.setOnClickListener(this);


        binding.loginGeneralLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register://获取验证码,倒计时
                switchUiToRegister();
                break;
            case R.id.login_general_login:
                //登录
                doLogin(binding.loginGeneralLogin.getText().toString().equals("登录"));
                break;
        }
    }

    /**
     * 切换ui到注册
     */
    private void switchUiToRegister() {
        if (binding.loginGeneralLogin.getText().toString().equals("登录")) {
            binding.loginGeneralLogin.setText("注册");
            binding.loginRegister.setText("去登录");
        } else {
            binding.loginGeneralLogin.setText("登录");
            binding.loginRegister.setText("去注册");
        }


    }

    private String loginMsg;

    /**
     * 普通用户帐号登录
     *
     * @param isLogin 是登录还是注册
     */
    private void doLogin(boolean isLogin) {
        ableBtn(false);
        Editable phoneNum = binding.loginGeneralPhone.getText();
        Editable pwd = binding.loginGeneralInputVerificationCode.getText();
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(pwd)) {
            ableBtn(true);
            showToast("帐号和密码不能为空！");
            return;
        }

        HashMap<String, String> params = new HashMap<>();

            params.put("phoneNum", phoneNum.toString());
            params.put("pwd", pwd.toString());

        Request request = isLogin ? Api.loginPost(this, params) : Api.registerPost(this, params);
        Call mCall = OkHttpManager.getInstance(this).getmOkHttpClient().newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loginMsg = getString(R.string.net_notify_fail);
                myHandler.sendEmptyMessage(what_code_fail);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e(TAG, "onResponse: 登录了" + json);
                JSONObject jsonObject = JSON.parseObject(json);
                if (jsonObject.getInteger("code")!=200) {
                    //失败
                    loginMsg = jsonObject.getString("msg");
                    myHandler.sendEmptyMessage(what_code_fail);
                } else {
                    //记录用户信息
                    User user = new User();
                    JSONObject result = jsonObject.getJSONObject("result");
                    user.setPhone(result.getString("phoneNumber"));
                    user.setRole(result.getString("role"));
                    user.setName("哈小子");
                    dao.insert(user);
                    finish2();
                }
            }
        });
    }

    /**
     * 按钮是否可点击
     *
     * @param able
     */
    private void ableBtn(boolean able) {
        binding.loginGeneralLogin.setClickable(able);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            myHandler.removeMessages(what_code_fail);
            myHandler.removeMessages(what_code_success);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Subscription mSubscription;

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(FinishRX.class)
                .subscribe(new Action1<FinishRX>() {
                    @Override
                    public void call(FinishRX what) {
                        if (what.getWhat().getCode() == 1)
                            finish();
                    }
                });
        //将订阅者加入管理站
        RxSubscriptions.add(mSubscription);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
    }
}

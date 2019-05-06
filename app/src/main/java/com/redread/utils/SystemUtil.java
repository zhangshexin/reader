package com.redread.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.redread.MyApplication;
import com.redread.model.entity.User;
import com.redread.model.gen.UserDao;

import java.util.List;

/**
 * Created by zhangshexin on 2018/9/21.
 */

public class SystemUtil {
    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void show_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


    public static boolean isAdamin() {
        UserDao dao = MyApplication.getInstances().getDaoSession().getUserDao();
        List<User> user = dao.loadAll();
        return user.get(0).getRole().equals("admin");

    }

    public static User getUser() {
        UserDao dao = MyApplication.getInstances().getDaoSession().getUserDao();
        List<User> user = dao.loadAll();
        if(user.size()>0)
            return user.get(0);
        return null;
    }
}

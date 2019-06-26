package com.redread.net;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhangshexin on 2018/9/25.
 */

public class Api {
    public static final String baseUrl = "http://172.16.47.205:8080";//这里填服务器的地址，不要用localhost或127.0.0.1

    /**
     * 普通登录
     * <p>
     * params:username,password
     */
    public static Request loginPost(Context mContext, HashMap params) {
        String paramsStr = paramToString(params);
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_NORAML_FORM, paramsStr);
        Request requestPost = new Request.Builder().url(baseUrl + "/ucenter/login").post(requestBody).build();
        return requestPost;
    }

    /**
     * 注册
     * name = "username", value = "登录用户名",
     * name = "pwd", value = "登录密码",
     *
     * @param mContext
     * @param params
     * @return
     */
    public static Request registerPost(Context mContext, HashMap params) {
        String paramsStr = paramToString(params);
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_NORAML_FORM, paramsStr);
        Request requestPost = new Request.Builder().url(baseUrl + "/ucenter/register").post(requestBody).build();
        return requestPost;
    }

    /**
     * 取专题列表
     * @param mContext
     * @param params
     * @return
     */
    public static Request specialListGet(Context mContext, int pageNum,int pageSize,int status) {
        Request request = new Request.Builder().url(baseUrl + "/special/list?pageNum="+pageNum+"&pageSize="+pageSize+"&status="+status).build();
        return request;
    }

    /**
     * 保存专题
     * @param mContext
     * @param params
     * @return
     */
    public static Request saveSpecialPost(Context mContext, String paramsStr) {
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_NORAML_FORM, paramsStr);
        Request requestPost = new Request.Builder().url(baseUrl + "/special/save").post(requestBody).build();
        return requestPost;
    }

    /**
     * 根据id删除
     * @param mContext
     * @param ids
     * @return
     */
    public static Request deleteSpecialByIdsPost(Context mContext, String ids) {
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_NORAML_FORM, ids);
        Request requestPost = new Request.Builder().url(baseUrl + "/special/deleteByIds").post(requestBody).build();
        return requestPost;
    }

    /**
     * 获取专题对应的所有考题
     * @param mContext
     * @param params
     * @return
     */
    public static Request specialExaminationGet(Context mContext, int specialId) {
        Request request = new Request.Builder().url(baseUrl + "/questions/all?specialId="+specialId).build();
        return request;
    }

    /**
     * 获取id对应的所有考题
     * @param mContext
     * @param params
     * @return
     */
    public static Request examinationByIdsGet(Context mContext, String ids) {
        Request request = new Request.Builder().url(baseUrl + "/questions/getIdsList?ids="+ids).build();
        return request;
    }



    /**
     * 返回对应专题的指定随机个数的考题
     * @param mContext
     * @param specialId
     * @param count
     * @return
     */
    public static Request examinationRandomIdsGet(Context mContext, int specialId,String count) {
        Request request = new Request.Builder().url(baseUrl + "/questions/random?specialId="+specialId+"&count="+count).build();
        return request;
    }

    /**
     * 保存考题
     * @param mContext
     * @param params
     * @return
     */
    public static Request saveExaminationPost(Context mContext, String paramsStr) {
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_NORAML_FORM, paramsStr);
        Request requestPost = new Request.Builder().url(baseUrl + "/questions/save").post(requestBody).build();
        return requestPost;
    }


    /**
     * 根据id删除
     * @param mContext
     * @param ids
     * @return
     */
    public static Request deleteExaminationByIdsPost(Context mContext, String ids) {
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_NORAML_FORM, ids);
        Request requestPost = new Request.Builder().url(baseUrl + "/questions/deleteByIds").post(requestBody).build();
        return requestPost;
    }


    /**
     * 获取用户的成绩，及排名
     * @param mContext
     * @param userId
     * @param specialId
     * @return
     */
    public static Request gradeAndRankGet(Context mContext,long userId, int specialId,int top) {
        Request request = new Request.Builder().url(baseUrl + "/grade/"+userId+"/"+specialId+"/"+top).build();
        return request;
    }

    /**
     * 更新用户成绩
     * @param mContext
     * @param id
     * @return
     */
    public static Request updateGradeByIdPost(Context mContext, long id,int specialId,int grade) {
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_NORAML_FORM, "userId="+id+"&specialId="+specialId+"&grade="+grade);
        Request requestPost = new Request.Builder().url(baseUrl + "/grade/saveOrUpdate").post(requestBody).build();
        return requestPost;
    }


    /**
     * 拼接参数
     *
     * @param data
     * @return
     */
    public static String paramToString(HashMap<String, String> data) {
        String url = "";
        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry ent = (Map.Entry) iterator.next();
            url = url + ent.getKey().toString() + "=" + ent.getValue().toString() + "&";
        }
        return url.substring(0, url.length() - 1);
    }
//    /**
//     * 手机号查重接口
//     *
//     * 参数：mobile
//     */
//    public static String registerMobileRepeatGet(Context mContext, HashMap params){
//        params.put("method","mx.user.mobile.repeat");
//        return ConfigParam.getInstance(mContext).commonGetApi(params);
//    }
//
//    /**
//     * 普通用户注册
//     * params:account,password
//     */
//    public static String registerAcountGet(Context mContext, HashMap params){
//        params.put("method","mx.user.account.register");
//        return ConfigParam.getInstance(mContext).commonGetApi(params);
//    }
//
//    /**
//     *
//     * 普通帐号是否存在
//     * params:account
//     */
//    public static String accountExistGet(Context mContext, HashMap params){
//        params.put("method","mx.user.account.exist");
//        return ConfigParam.getInstance(mContext).commonGetApi(params);
//    }
}

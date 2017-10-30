package com.qwy.demo.qrscandemo.network;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.qwy.demo.qrscandemo.R;
import com.qwy.demo.qrscandemo.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;

/**
 * Created by qwy on 16/6/19.
 * 网络请求管理类
 */
public class HttpManager{

//    String tag;

    private static HttpManager instance = null;

    public static HttpManager getInstance(){
        if(instance == null){
            synchronized (HttpManager.class) {
                if(instance == null){
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    public void post(final Context context, String url, RequestParams params, final HttpCallback callback){
        if(!Utility.hasNetwork(context)){
            Utility.shortToast(context, R.string.net_error);
            return ;
        }
        final int flag = url.hashCode() % 7777;
        final String tag = "post " + flag;
        Utility.log(tag + " url: " + url + "?" + params.toString());
        HttpRequest.post(url, params, new BaseHttpRequestCallback() {

                    public void onStart() {
                        if (callback == null) {
                            return;
                        }
                        callback.onStart();
                    }

                    public void onResponse(String response, Headers headers) {
                        Utility.log(tag + " response: " + response);
                        if (callback == null) {
                            return;
                        }
                        if(TextUtils.isEmpty(response) || response.length() <= 2){
                            return ;
                        }else if(!response.startsWith("{")){
                            // 接口返回数据格式不正确
                            return ;
                        }
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("errorcode");
                            if (code == 0) {
                                // 请求成功
                                callback.onResponse(code, headers, response);
                            }else {
                                // 请求失败,打印错误信息
                                String msg = "";
                                if(jsonObject.has("errormsg")) {
                                    msg = jsonObject.getString("errormsg");
                                    Utility.shortToast(context, jsonObject.getString("errormsg"));
                                }
                                callback.onFailure(code, msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFailure(int errorCode, String msg) {
                        if (callback == null) {
                            return;
                        }
                        callback.onFailure(errorCode, msg);
                    }

                    public void onFinish() {
                        if (callback == null) {
                            return;
                        }
                        callback.onFinish();
                    }

                    /**
                     * 上传文件进度
                     * @param progress
                     * @param networkSpeed 网速
                     * @param done
                     */
                    public void onProgress(int progress, long networkSpeed, boolean done) {
                        if (callback == null) {
                            return;
                        }
                        callback.onProgress(progress, networkSpeed, done);
                    }

                }
        );
    }

    public void post1(final Context context, String url, RequestParams params, final HttpCallback callback){
        if(!Utility.hasNetwork(context)){
            Utility.shortToast(context, R.string.net_error);
            return ;
        }
        final int flag = url.hashCode() % 7777;
        final String tag = "post " + flag;
        Utility.log(tag + " url: " + url + params.toString());
        HttpRequest.post(url, params, new BaseHttpRequestCallback() {

                    public void onStart() {
                        if (callback == null) {
                            return;
                        }
                        callback.onStart();
                    }

                    public void onResponse(String response, Headers headers) {
                        Utility.log(tag + " response: " + response);
                        if (callback == null) {
                            return;
                        }
                        if(TextUtils.isEmpty(response) || response.length() <= 2){
                            return ;
                        }else if(!response.startsWith("{")){
                            // 接口返回数据格式不正确
                            return ;
                        }
                        callback.onResponse(0, headers, response);
                    }

                    public void onFailure(int errorCode, String msg) {
                        if (callback == null) {
                            return;
                        }
                        callback.onFailure(errorCode, msg);
                    }

                    public void onFinish() {
                        if (callback == null) {
                            return;
                        }
                        callback.onFinish();
                    }

                    /**
                     * 上传文件进度
                     * @param progress
                     * @param networkSpeed 网速
                     * @param done
                     */
                    public void onProgress(int progress, long networkSpeed, boolean done) {
                        if (callback == null) {
                            return;
                        }
                        callback.onProgress(progress, networkSpeed, done);
                    }

                }
        );
    }

}

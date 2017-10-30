package com.qwy.demo.qrscandemo.network;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Headers;

/**
 * Created by qwy on 16/6/19.
 * 请求回调
 */
public abstract class HttpCallback<T> {
    protected Type t;
    public HttpCallback() {
        t = getModelClazz(getClass());
    }

    Type getModelClazz(Class<?> subclass) {
        return getGenericType(0, subclass);
    }

    private Type getGenericType(int index, Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) superclass).getActualTypeArguments();
        if (index >= params.length || index < 0) {
//                throw new RuntimeException("Index outof bounds");
            return null;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return params[index];
    }

    public abstract void onStart();

    public abstract void onResponse(int code, Headers headers, T response);

    public void onResponse(int code, String message, T response){};

    public abstract void onFinish();

    protected void onSuccess(int code, Headers headers, T t) {
    }
    protected void onSuccess(T t) {

    }

    /**
     * 上传文件进度
     * @param progress
     * @param networkSpeed 网速
     * @param done
     */
    public void onProgress(int progress, long networkSpeed, boolean done){
    }

    public void onFailure(int errorCode, String msg) {
    }

}

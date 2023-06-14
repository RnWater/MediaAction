package com.henry.audio;

import android.text.TextUtils;

public class NativeLib {
    static {
        System.loadLibrary("audio");
    }
    private OnErrorListener listener;
    private OnCallPrepared preparedListener;
    public void setErrorListener(OnErrorListener listener){
        this.listener = listener;
    }
    public void setPrepareListener(OnCallPrepared listener){
        this.preparedListener = listener;
    }
    public native void createChildThread();
    /***
     * 不需要调用
     * @param errorCode
     * @param errorMsg
     */
    public void onError(String errorCode,String errorMsg){
        if (listener != null) {
            listener.onError(errorCode, errorMsg);
        }
    }

    /**
     * 初始化完成
     */
    public void onPrepare(){
        if (preparedListener != null) {
            prepared = true;
            preparedListener.onPrepared();
        }
    }

    private String source;
    private boolean prepared;
    public void setSource(String source){
        this.source = source;
    }
    public void prepare(){
        if (TextUtils.isEmpty(source)) {
            return;
        }
        prepared = false;
        n_prepare(source);
    }
     public void start() {
        if (prepared) {
            n_start();
        }
    }

    public void release() {
        n_release();
    }
    public native void n_prepare(String source);
    public native void n_start();
    public native void n_release();
}
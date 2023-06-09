package com.henry.audio;

public class NativeLib {
    static {
        System.loadLibrary("audio");
    }
    private OnErrorListener listener;
    public void setErrorListener(OnErrorListener listener){
        this.listener = listener;
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
}
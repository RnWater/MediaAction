package com.henry.audio;

public class NativeLib {
    static {
        System.loadLibrary("audio");
    }
    public native String stringFromJNI();
}
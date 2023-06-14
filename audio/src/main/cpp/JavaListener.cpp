//
// Created by Henry on 2023/6/9.
//

#include "JavaListener.h"

JavaListener::JavaListener(JavaVM *jvm, _JNIEnv *env, jobject obj) {
    this->jvm = jvm;
    this->job = obj;
    this->env = env;
    initOnError();
    initPrepare();
}

JavaListener::~JavaListener() {
}

void JavaListener::initPrepare() {
    jclass clz = env->GetObjectClass(job);
    if (!clz) {
        return;
    }
    jmidPrepare = env->GetMethodID(clz, "onPrepare", "()V");
}

void JavaListener::initOnError() {
    jclass clz = env->GetObjectClass(job);
    if (!clz) {
        return;
    }
    jmidError = env->GetMethodID(clz, "onError", "(Ljava/lang/String;Ljava/lang/String;)V");
}

void JavaListener::onError(int threadType, const char *code, const char *msg) {
    if (threadType == 0) {
        JNIEnv *envInner;
        jvm->AttachCurrentThread(&envInner, 0);
        jstring jCode = envInner->NewStringUTF(code);
        jstring jMsg = envInner->NewStringUTF(msg);
        envInner->CallVoidMethod(job, jmidError, jCode, jMsg);
        envInner->DeleteLocalRef(jCode);
        envInner->DeleteLocalRef(jMsg);
        jvm->DetachCurrentThread();
    } else if (threadType == 1) {
        jstring jCode = env->NewStringUTF(code);
        jstring jMsg = env->NewStringUTF(msg);
        env->CallVoidMethod(job, jmidError, jCode, jMsg);
        env->DeleteLocalRef(jCode);
        env->DeleteLocalRef(jMsg);
    }
}
void JavaListener::onPrepare() {
    JNIEnv *envInner;
    jvm->AttachCurrentThread(&envInner, 0);
    envInner->CallVoidMethod(job, jmidPrepare);
    jvm->DetachCurrentThread();
}

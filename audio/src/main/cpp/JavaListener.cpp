//
// Created by Henry on 2023/6/9.
//

#include "JavaListener.h"
JavaListener::JavaListener(JavaVM *jvm, _JNIEnv *env, jobject obj) {
    this->jvm = jvm;
    this->job = obj;
    this->env = env;
    jclass clz = env->GetObjectClass(obj);
    if (!clz) {
        return;
    }
    jmid = env->GetMethodID(clz, "onError", "(Ljava/lang/String;Ljava/lang/String;)V");
}

JavaListener::~JavaListener() {

}

void JavaListener::onError(int threadType, const char *code, const char *msg) {
    jstring jCode = env->NewStringUTF(code);
    jstring jMsg = env->NewStringUTF(msg);
    if (threadType == 0) {
        JNIEnv *envInner;
        jvm->AttachCurrentThread(&envInner, 0);
        envInner->CallVoidMethod(job, jmid, jCode, jMsg);
        jvm->DetachCurrentThread();
    } else if (threadType == 1) {
        env->CallVoidMethod(job, jmid, jCode, jMsg);
    }
    env->DeleteLocalRef(jCode);
    env->DeleteLocalRef(jMsg);
}

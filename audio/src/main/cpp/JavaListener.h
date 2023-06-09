//
// Created by Henry on 2023/6/9.
//

#ifndef MEDIAACTION_JAVALISTENER_H
#define MEDIAACTION_JAVALISTENER_H
#include <jni.h>
class JavaListener {
public:
    JavaVM *jvm;
    _JNIEnv *env;
    jobject job;
    jmethodID jmid;
public:
    JavaListener(JavaVM *jvm,_JNIEnv *env,jobject obj);
    ~JavaListener();
    void onError(int threadType, const char *code, const char *msg);
};


#endif //MEDIAACTION_JAVALISTENER_H

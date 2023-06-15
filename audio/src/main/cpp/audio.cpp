#include <jni.h>
#include <string>
#include "thread"
#include "queue"
#include "unistd.h"
#include <iostream>
extern "C" {
#include "libavcodec/avcodec.h"
}

#include "FFLog.h"
#include "JavaListener.h"
#include "FFUnPacking.h"

using namespace std;
pthread_t thread;
pthread_t product;
pthread_t custom;
pthread_mutex_t mutex_thread;
pthread_cond_t cond_thread;
queue<int> queues;

void *customThreadCallBack(void *data) {
    while (1) {
        pthread_mutex_lock(&mutex_thread);
        if (queues.size() > 0) {
            queues.pop();
            LOGE("消费者消费产品%d", queues.size());
        } else {
            pthread_cond_wait(&cond_thread, &mutex_thread);
        }
        pthread_mutex_unlock(&mutex_thread);
        usleep(1000 * 500);
    }
}

void *productThreadCallBack(void *data) {
    while (1) {
        pthread_mutex_lock(&mutex_thread);
        queues.push(1);
        LOGE("生产者生产产品%lu", queues.size());
        pthread_cond_signal(&cond_thread);//通知消费线程
        pthread_mutex_unlock(&mutex_thread);
        sleep(2);
    }
}

JavaVM *javaVm;

void loadChildThread(JavaListener *listener) {
    const char *code = "S00001";
    const char *msg = "工作线程调用";
    listener->onError(0, code, msg);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_henry_audio_NativeLib_createChildThread(JNIEnv *env, jobject thiz) {
    pthread_mutex_init(&mutex_thread, NULL);
    pthread_cond_init(&cond_thread, NULL);
    pthread_create(&custom, NULL, customThreadCallBack, NULL);
    pthread_create(&product, NULL, productThreadCallBack, NULL);

    JavaListener *listener = new JavaListener(javaVm, env, env->NewGlobalRef(thiz));
    std::thread childThread(loadChildThread, listener);
    childThread.detach();
}
extern "C"
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    javaVm = vm;
    return JNI_VERSION_1_6;
}

FFUnPacking *ffUnPacking=NULL;
JavaListener *listener = NULL;
FFPlayStatus *playStatus=NULL;
extern "C"
JNIEXPORT void JNICALL
Java_com_henry_audio_NativeLib_n_1prepare(JNIEnv *env, jobject thiz, jstring source) {
    LOGE("我的打印");
    const char *audioSource = env->GetStringUTFChars(source, NULL);
    if (listener == NULL) {
        listener = new JavaListener(javaVm, env, env->NewGlobalRef(thiz));
    }
    if (playStatus == NULL) {
        playStatus = new FFPlayStatus();
    }
    if (ffUnPacking == NULL) {
        ffUnPacking = new FFUnPacking(listener, playStatus);
    }
    ffUnPacking->setMediaSource(audioSource);
    ffUnPacking->prepare();
    LOGE("执行完Prepare");
//    env->ReleaseStringUTFChars(source, audioSource);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_henry_audio_NativeLib_n_1start(JNIEnv *env, jobject thiz) {
    if (ffUnPacking) {
        ffUnPacking->start();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_henry_audio_NativeLib_n_1release(JNIEnv *env, jobject thiz) {
    playStatus->exit = true;
    if (ffUnPacking) {
        delete ffUnPacking;
    }
}
#include <jni.h>
#include <string>
#include "thread"
#include "queue"
#include "unistd.h"
extern "C" {
#include "libavcodec/avcodec.h"
}
#include "FFLog.h"
#include "JavaListener.h"

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
            LOGE("消费者消费产品%d",queues.size());
        } else{
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
        LOGE("生产者生产产品%d",queues.size());
        pthread_cond_signal(&cond_thread);//通知消费线程
        pthread_mutex_unlock(&mutex_thread);
        sleep(2);
    }
}
JavaVM *javaVm;
extern "C"
JNIEXPORT void JNICALL
Java_com_henry_audio_NativeLib_createChildThread(JNIEnv *env, jobject thiz) {
    pthread_mutex_init(&mutex_thread, NULL);
    pthread_cond_init(&cond_thread, NULL);
    pthread_create(&custom, NULL, customThreadCallBack, NULL);
    pthread_create(&product, NULL, productThreadCallBack, NULL);

    JavaListener *listener = new JavaListener(javaVm, env, thiz);
    const char *code = "S00001";
    const char *msg = "主线程调用";
    listener->onError(1, code, msg);

}
extern "C"
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved){
    JNIEnv* env;
    if (vm->GetEnv((void**)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    javaVm = vm;
    return JNI_VERSION_1_6;
}
#include <jni.h>
#include <string>
extern "C"{
#include "libavcodec/avcodec.h"
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_henry_audio_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    const char *configuration = avcodec_configuration();
    return env->NewStringUTF(configuration);
}
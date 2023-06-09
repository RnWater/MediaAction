//
// Created by Henry on 2023/6/9.
//

#ifndef MEDIAACTION_FFLOG_H
#define MEDIAACTION_FFLOG_H
#define TAG "FFMPEG_LOG"
#ifdef ANDROID
#include <android/log.h>
#define LOGE(...)__android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)
#else
#define LOGD(...) printf("MediaAction",__VA_ARGS__)
#endif
#endif //MEDIAACTION_FFLOG_H

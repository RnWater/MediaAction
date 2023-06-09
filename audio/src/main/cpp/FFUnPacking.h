//
// Created by Henry on 2023/6/9.
//

#ifndef MEDIAACTION_FFUNPACKING_H
#define MEDIAACTION_FFUNPACKING_H

#include "FFLog.h"
#include "JavaListener.h"
#include "thread"
#include "FFAudio.h"
extern "C" {
#include "libavformat/avformat.h"
};

class FFUnPacking {
public:
    JavaListener *javaListener = NULL;
    char* audioCopy;
    const char *audioUrl;
    AVFormatContext *pFormatContext = NULL;
    FFAudio *audioBean = NULL;
    FFPlayStatus *playStatus=NULL;
public:
    FFUnPacking(JavaListener *listener,FFPlayStatus *status);
    ~FFUnPacking();
    void setMediaSource(const char *url);
    void prepare();
    void clearResource();
    void unPackageAudio();
    void start();
    void release();
};


#endif //MEDIAACTION_FFUNPACKING_H

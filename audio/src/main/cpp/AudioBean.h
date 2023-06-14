//
// Created by Henry on 2023/6/12.
//

#ifndef MEDIAACTION_AUDIOBEAN_H
#define MEDIAACTION_AUDIOBEAN_H

#include "FFQueue.h"
#include "FFPlayStatus.h"
extern "C"{
    #include "libavcodec/avcodec.h"
};
class AudioBean {
public:
    int streamIndex=-1;
    AVCodecParameters *audioCodecParameters = NULL;
    AVCodecContext *avCodecContext = NULL;
    FFQueue *audioQueue = NULL;
    FFPlayStatus *playStatus = NULL;
public:
    AudioBean(FFPlayStatus *playStatus);
    ~AudioBean();
};


#endif //MEDIAACTION_AUDIOBEAN_H

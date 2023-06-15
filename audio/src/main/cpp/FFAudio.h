//
// Created by Henry on 2023/6/12.
//

#ifndef MEDIAACTION_FFAUDIO_H
#define MEDIAACTION_FFAUDIO_H

#include "FFQueue.h"
#include "FFPlayStatus.h"
extern "C"{
    #include "libavcodec/avcodec.h"
    #include <libswresample/swresample.h>
};
#include "SLES/OpenSLES.h"
#include "SLES/OpenSLES_Android.h"
#define BUFFER_SIZE 4096
class FFAudio {
public:
    int streamIndex=-1;
    AVCodecParameters *audioCodecParameters = NULL;
    AVCodecContext *avCodecContext = NULL;
    FFQueue *audioQueue = NULL;
    FFPlayStatus *playStatus = NULL;
    //引擎对象
    SLObjectItf objectItf = NULL;
    SLEngineItf engineItf = NULL;
    //混音器对象
    SLObjectItf outputMixItf=NULL;
    // 创建播放器对象
    SLObjectItf playerObject;
    SLEnvironmentalReverbItf slEnvironmentalReverbItf=NULL;
    SLEnvironmentalReverbSettings reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;
     // 获取播放器接口
    SLPlayItf playerInterface;
    // 获取缓冲队列接口
    SLAndroidSimpleBufferQueueItf bufferQueueInterface;
    AVPacket *avPacket = NULL;
    AVFrame *avFrame = NULL;
public:
    FFAudio(FFPlayStatus *playStatus);
    ~FFAudio();

    void startPlay();
    void pausePlay();
    void stopPlay();
    void resampleAudio();
};


#endif //MEDIAACTION_FFAUDIO_H

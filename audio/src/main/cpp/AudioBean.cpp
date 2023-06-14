//
// Created by Henry on 2023/6/12.
//

#include "AudioBean.h"

AudioBean::AudioBean(FFPlayStatus *playStatus) {
    this->playStatus = playStatus;
    audioQueue = new FFQueue(playStatus);
}

AudioBean::~AudioBean() {
    if (!avCodecContext) {
        avcodec_free_context(&avCodecContext);
    }
}

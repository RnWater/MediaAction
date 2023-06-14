//
// Created by Henry on 2023/6/14.
//

#ifndef MEDIAACTION_FFQUEUE_H
#define MEDIAACTION_FFQUEUE_H
extern "C" {
#include <libavcodec/packet.h>
#include "libavformat/avformat.h"
}

#include "FFLog.h"
#include "thread"
#include "queue"
#include "FFPlayStatus.h"

using namespace std;

class FFQueue {
public:
    queue<AVPacket *> queuePacket;
    pthread_cond_t condPacket;
    pthread_mutex_t mutexPacket;
    FFPlayStatus *playStatus = NULL;
public:
    FFQueue(FFPlayStatus *playStatus);

    ~FFQueue();

    int putAvPacket(AVPacket *avPacket);

    int getAvPacket(AVPacket *avPacket);

    int getQueueSize();
};


#endif //MEDIAACTION_FFQUEUE_H

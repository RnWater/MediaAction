//
// Created by Henry on 2023/6/14.
//

#include "FFQueue.h"

int FFQueue::putAvPacket(AVPacket *avPacket) {
    pthread_mutex_lock(&mutexPacket);
    queuePacket.push(avPacket);
    pthread_cond_signal(&condPacket);
    pthread_mutex_unlock(&mutexPacket);
    return 0;
}

int FFQueue::getAvPacket(AVPacket *avPacket) {
    pthread_mutex_lock(&mutexPacket);
    while (playStatus != NULL && !playStatus->exit) {
        if (queuePacket.size() > 0) {
            AVPacket *packet = queuePacket.front();
            //引用拷贝data
            if (av_packet_ref(avPacket, packet) == 0) {
                queuePacket.pop();
            }
            av_packet_free(&packet);
            av_free(packet);
            packet = NULL;
            if (queuePacket.size() < 100) {
                LOGE("从队列中取出一个,队列中还剩%d", queuePacket.size());
            }
            break;
        } else {
            pthread_cond_wait(&condPacket, &mutexPacket);
        }
    }
    pthread_mutex_unlock(&mutexPacket);
    return 0;
}

FFQueue::FFQueue(FFPlayStatus *playStatus) {
    this->playStatus = playStatus;
    pthread_mutex_init(&mutexPacket, NULL);
    pthread_cond_init(&condPacket, NULL);
}

FFQueue::~FFQueue() {
    pthread_mutex_destroy(&mutexPacket);
}

int FFQueue::getQueueSize() {
    int size = 0;
    pthread_mutex_lock(&mutexPacket);
    size = queuePacket.size();
    pthread_mutex_unlock(&mutexPacket);
    return size;
}

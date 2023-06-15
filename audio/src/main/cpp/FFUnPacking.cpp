//
// Created by Henry on 2023/6/9.
//

#include "FFUnPacking.h"

FFUnPacking::~FFUnPacking() {
    clearResource();
    release();
}

void FFUnPacking::release() {
    LOGE("界面退出");
    delete javaListener;
    this->javaListener = NULL;
    this->audioUrl = NULL;
    delete audioBean;
    this->audioBean = NULL;
    delete playStatus;
    this->playStatus = NULL;
     if (audioCopy) {
        LOGE("删除数据");
        delete audioCopy;
    }
}

FFUnPacking::FFUnPacking(JavaListener *listener, FFPlayStatus *status) {
    this->javaListener = listener;
    playStatus = status;
}

/**
 * 解封装操作
 */
void unPack(FFUnPacking *ffUnPacking) {
    ffUnPacking->unPackageAudio();
}

void FFUnPacking::prepare() {
    std::thread unPacking(unPack, this);
    unPacking.detach();
}

void FFUnPacking::unPackageAudio() {
    avformat_network_init();
    pFormatContext = avformat_alloc_context();
    int ret = 0;
    ret = avformat_open_input(&pFormatContext, audioUrl, NULL, NULL);
    char buf[2048];
    if (ret != 0) {
        av_strerror(ret, buf, 1024);
        LOGE("couldn't open file : %s%d(%s)", audioUrl, ret, buf);
        return;
    }
    if (avformat_find_stream_info(pFormatContext, NULL) < 0) {
        LOGE("can find_stream_info url %s", audioUrl);
        return;
    }
    if (!audioBean) {
        audioBean = new FFAudio(playStatus);
    }
    audioBean->streamIndex = av_find_best_stream(pFormatContext,
                                                 AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
    audioBean->audioCodecParameters = pFormatContext->streams[audioBean->streamIndex]->codecpar;

    const AVCodec *avCodec = avcodec_find_decoder(audioBean->audioCodecParameters->codec_id);
    if (!avCodec) {
        LOGE("can not find avcodec decoder url %s", audioUrl);
        return;
    }
    audioBean->avCodecContext = avcodec_alloc_context3(avCodec);
    if (!audioBean->avCodecContext) {
        LOGE("can not avcodec_alloc_context3 url %s", audioUrl);
        return;
    }
    ret = avcodec_parameters_to_context(audioBean->avCodecContext,
                                        audioBean->audioCodecParameters);
    if (ret < 0) {
        LOGE("can not fill decode url %s", audioUrl);
        return;
    }
    ret = avcodec_open2(audioBean->avCodecContext, avCodec, 0);
    if (ret != 0) {
        LOGE("can not open audio url %s", audioUrl);
        return;
    }
    javaListener->onPrepare();
}

void FFUnPacking::setMediaSource(const char *url) {
    clearResource();
    int len = strlen(url);
    audioCopy = (char *) malloc((len + 1) * sizeof(char));
    strcpy(audioCopy, url);
    this->audioUrl = audioCopy;
    LOGE("my audio url is %s", audioUrl);
}

void FFUnPacking::clearResource() {
    if (pFormatContext) {
        avformat_free_context(pFormatContext);
        pFormatContext = NULL;
    }
}

void FFUnPacking::start() {
    if (audioBean == NULL) {
        return;
    }
    int count = 0;
    while (playStatus!=NULL&&!playStatus->exit) {
        AVPacket *avPacket = av_packet_alloc();
        if (pFormatContext!=NULL) {
            if (av_read_frame(pFormatContext, avPacket) == 0) {
                if (audioBean!=NULL) {
                    if (avPacket->stream_index == audioBean->streamIndex) {
                        count++;
                        audioBean->audioQueue->putAvPacket(avPacket);
                    } else {
                        av_packet_free(&avPacket);
                        av_free(avPacket);
                        avPacket = NULL;
                    }
                }
            } else {
                av_packet_free(&avPacket);
                av_free(avPacket);
                avPacket = NULL;
                break;
            };
        }
    }
    audioBean->startPlay();
}

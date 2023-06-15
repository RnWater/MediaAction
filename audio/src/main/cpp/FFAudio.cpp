//
// Created by Henry on 2023/6/12.
//

#include "FFAudio.h"

void playerCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    // 从音频数据源中读取数据
    // 将数据填充到播放器缓冲区中
    // 这里需要注意，每次读取的数据大小必须是BUFFER_SIZE的整数倍
    // 如果读取的数据不足BUFFER_SIZE，则需要填充0来保证数据的完整性
    FFAudio *audioBean = (FFAudio *) context;
    while (audioBean->audioQueue->getQueueSize() > 0 && !audioBean->playStatus->exit) {
        LOGE("播放");
        AVPacket *recAvPacket = av_packet_alloc();
        audioBean->audioQueue->getAvPacket(recAvPacket);


        av_packet_free(&recAvPacket);
        av_free(recAvPacket);
        recAvPacket = NULL;
    }
}

FFAudio::FFAudio(FFPlayStatus *playStatus) {
    this->playStatus = playStatus;
    audioQueue = new FFQueue(playStatus);
    //创建引擎对象
    slCreateEngine(&objectItf, 0, 0, 0, 0, 0);
    (*objectItf)->Realize(objectItf, SL_BOOLEAN_FALSE);
    (*objectItf)->GetInterface(objectItf, SL_IID_ENGINE, &engineItf);
    //创建混音器
    const SLInterfaceID mids[1] = {SL_IID_ENVIRONMENTALREVERB};
    const SLboolean mreq[1] = {SL_BOOLEAN_FALSE};
    (*engineItf)->CreateOutputMix(engineItf, &outputMixItf, 1, mids, mreq);
    (*outputMixItf)->Realize(outputMixItf, SL_BOOLEAN_FALSE);
    (*outputMixItf)->GetInterface(outputMixItf, SL_IID_ENVIRONMENTALREVERB,
                                  &slEnvironmentalReverbItf);
    (*slEnvironmentalReverbItf)->SetEnvironmentalReverbProperties(slEnvironmentalReverbItf,
                                                                  &reverbSettings);

    SLDataLocator_AndroidSimpleBufferQueue locatorBufferQueue = {
            SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};
    SLDataFormat_PCM formatPCM = {SL_DATAFORMAT_PCM,
                                  1,
                                  SL_SAMPLINGRATE_44_1,
                                  SL_PCMSAMPLEFORMAT_FIXED_16,
                                  SL_PCMSAMPLEFORMAT_FIXED_16,
                                  SL_SPEAKER_FRONT_CENTER,
                                  SL_BYTEORDER_LITTLEENDIAN};
    SLDataSource audioSrc = {&locatorBufferQueue, &formatPCM};
    SLDataLocator_OutputMix locatorOutputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixItf};
    SLDataSink audioSnk = {&locatorOutputMix, NULL};
    const SLInterfaceID ids[1] = {SL_IID_BUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};

    (*engineItf)->CreateAudioPlayer(engineItf, &playerObject, &audioSrc, &audioSnk, 1, ids, req);
    // 初始化播放器对象
    (*playerObject)->Realize(playerObject, SL_BOOLEAN_FALSE);
    (*playerObject)->GetInterface(playerObject, SL_IID_PLAY, &playerInterface);


    (*playerObject)->GetInterface(playerObject, SL_IID_BUFFERQUEUE, &bufferQueueInterface);

    // 注册缓冲队列回调函数
    (*bufferQueueInterface)->RegisterCallback(bufferQueueInterface, playerCallback, NULL);
}


FFAudio::~FFAudio() {
    if (!avCodecContext) {
        avcodec_free_context(&avCodecContext);
        av_free(avCodecContext);
        avCodecContext = NULL;
    }
    if (!playStatus) {
        playStatus->exit = true;
    }
    if (!audioCodecParameters) {
        av_free(audioCodecParameters);
    }
}

void FFAudio::startPlay() {
    // 开始播放
    (*playerInterface)->SetPlayState(playerInterface, SL_PLAYSTATE_PLAYING);
    // 向缓冲队列中写入数据
    playerCallback(bufferQueueInterface, this);
}

void FFAudio::pausePlay() {
    (*playerInterface)->SetPlayState(playerInterface, SL_PLAYSTATE_PAUSED);
}

void FFAudio::stopPlay() {
    (*playerInterface)->SetPlayState(playerInterface, SL_PLAYSTATE_STOPPED);
}

void FFAudio::resampleAudio() {
    while (playStatus != NULL && !playStatus->exit) {
        avPacket = av_packet_alloc();
        if (audioQueue->getAvPacket(avPacket) != 0) {
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            continue;
        }
        int ret = avcodec_send_packet(avCodecContext, avPacket);
        if (ret != 0) {
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            continue;
        }
        avFrame = av_frame_alloc();
        ret = avcodec_receive_frame(avCodecContext, avFrame);
        if (ret != 0) {
            //先释放packet
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            //释放frame
            av_frame_free(&avFrame);
            av_free(avFrame);
            avFrame = NULL;
            continue;
        }
        if (avFrame->channels > 0 && avFrame->channel_layout == 0) {
            avFrame->channel_layout = av_get_default_channel_layout(avFrame->channels);
        }else if(avFrame->channels == 0 && avFrame->channel_layout > 0){
            avFrame->channels = av_get_channel_layout_nb_channels(avFrame->channel_layout);
        }
        //重采样
        SwrContext *swrContext = swr_alloc();
        swr_alloc_set_opts(swrContext, AV_CH_LAYOUT_STEREO, AV_SAMPLE_FMT_S16, avFrame->sample_rate,
                           avFrame->channel_layout, (AVSampleFormat) avFrame->format,
                           avFrame->sample_rate,
                           NULL, NULL);
        if (!swrContext || swr_init(swrContext)<0) {

        }
    }
}

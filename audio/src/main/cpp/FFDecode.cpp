//
// Created by Henry on 2023/6/9.
//

#include "FFDecode.h"
extern "C"{
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
}
FFDecode::FFDecode() {

}

FFDecode::~FFDecode() {
    if (!avCodecContext) {
        avcodec_free_context(&avCodecContext);
    }
}

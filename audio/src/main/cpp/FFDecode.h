//
// Created by Henry on 2023/6/9.
//

#ifndef MEDIAACTION_FFDECODE_H
#define MEDIAACTION_FFDECODE_H

struct AVCodecContext;
class FFDecode {
public:
    FFDecode();
    ~FFDecode();
    AVCodecContext *avCodecContext;
};


#endif //MEDIAACTION_FFDECODE_H

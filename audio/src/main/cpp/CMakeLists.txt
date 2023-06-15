cmake_minimum_required(VERSION 3.18.1)
project("audio")

#添加头文件路径
include_directories(../../../myJni/include)
include_directories(../../../myJni/soundtouch/include)
include_directories(../../../myJni/soundtouch/SoundTouch)
#设置ffmpeg库路径变量
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../../../libs/${ANDROID_ABI}")
set(SOUNDTOUCH ${CMAKE_CURRENT_SOURCE_DIR}/../../../myJni/soundtouch/SoundTouch)
add_library(
        audio
        SHARED
        audio.cpp
        ${SOUNDTOUCH}/AAFilter.cpp
        ${SOUNDTOUCH}/FIFOSampleBuffer.cpp
        ${SOUNDTOUCH}/FIRFilter.cpp
        ${SOUNDTOUCH}/cpu_detect_x86.cpp
        ${SOUNDTOUCH}/sse_optimized.cpp
        ${SOUNDTOUCH}/RateTransposer.cpp
        ${SOUNDTOUCH}/SoundTouch.cpp
        ${SOUNDTOUCH}/InterpolateCubic.cpp
        ${SOUNDTOUCH}/InterpolateLinear.cpp
        ${SOUNDTOUCH}/InterpolateShannon.cpp
        ${SOUNDTOUCH}/TDStretch.cpp
        ${SOUNDTOUCH}/PeakFinder.cpp
        FFDecode.cpp
        FFUnPacking.cpp
        JavaListener.cpp
        FFAudio.cpp
        FFQueue.cpp
        FFPlayStatus.cpp
)
find_library(
        log-lib
        log)
target_link_libraries(
        audio
        GLESv2 EGL OpenSLES
        android
        avcodec
        avdevice
        avfilter
        avformat
        avutil
        swresample
        swscale
        ${log-lib})
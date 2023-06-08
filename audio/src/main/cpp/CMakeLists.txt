cmake_minimum_required(VERSION 3.18.1)
project("audio")

#添加头文件路径
include_directories(../../../myJni/include)
include_directories(../../../myJni/soundtouch/include)
include_directories(../../../myJni/soundtouch/SoundTouch)
#设置ffmpeg库路径变量
set(FF ${CMAKE_CURRENT_SOURCE_DIR}/../../../myJni/${ANDROID_ABI})
set(SOUNDTOUCH ${CMAKE_CURRENT_SOURCE_DIR}/../../../myJni/soundtouch/SoundTouch)

add_library(avcodec SHARED IMPORTED)
add_library(avformat SHARED IMPORTED)
add_library(avutil SHARED IMPORTED)
add_library(avfilter SHARED IMPORTED)
add_library(avdevice SHARED IMPORTED)
add_library(swscale SHARED IMPORTED)
add_library(swresample SHARED IMPORTED)

set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${FF}/libavcodec.so)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${FF}/libavformat.so)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${FF}/libavutil.so)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${FF}/libswscale.so)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${FF}/libswresample.so)
set_target_properties(avdevice PROPERTIES IMPORTED_LOCATION ${FF}/libavdevice.so)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${FF}/libavfilter.so)

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
)
find_library(
        log-lib
        log)
target_link_libraries(
        audio
        GLESv2 EGL OpenSLES
        android
        avcodec avformat avutil swscale swresample avfilter avdevice
        ${log-lib})
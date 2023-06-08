package com.henry.mediaaction.base;

public interface BaseViewInter<T extends BaseResponse> {
    /**
     * @param type 对应的接口
     * @param t 对应的返回参数
     */
    void actionSuccess(String type, T... t);
    void doFailure(String type,T... t);
}

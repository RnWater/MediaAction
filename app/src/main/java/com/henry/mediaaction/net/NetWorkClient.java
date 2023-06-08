package com.henry.mediaaction.net;

import com.henry.mediaaction.base.BaseModelInter;
import com.henry.mediaaction.base.BaseResponse;
import com.henry.mediaaction.bean.MusicBean;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Observer;
public class NetWorkClient extends BaseModelInter {
    private static NetWorkClient client;
    private NetWorkClient(){}
    public static NetWorkClient getInstance() {
        if (client == null) {
            synchronized (NetWorkClient.class) {
                if (client == null) {
                    client = new NetWorkClient();
                }
            }
        }
        return client;
    }
    /**
     * 获取音乐接口
     * @param observer
     */
    public void getMusics(Observer<BaseResponse<List<MusicBean>>> observer){
        Observable observable = mApis.getMusics();
        toSubscribe(observable,observer);
    }
}

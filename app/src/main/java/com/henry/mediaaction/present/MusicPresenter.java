package com.henry.mediaaction.present;
import com.henry.mediaaction.base.BaseObserver;
import com.henry.mediaaction.base.BasePresenter;
import com.henry.mediaaction.base.BaseResponse;
import com.henry.mediaaction.base.BaseViewInter;
import com.henry.mediaaction.bean.MusicBean;
import com.henry.mediaaction.net.NetWorkClient;
import java.util.List;
public class MusicPresenter extends BasePresenter<BaseViewInter> {
    public void getMusics() {
        NetWorkClient.getInstance().getMusics(new BaseObserver<BaseResponse<List<MusicBean>>>() {
            @Override
            protected void _onError(Throwable e) {
                if (getView() != null) {
                    getView().doFailure("getMusics");
                }
            }
            @Override
            protected void _onNext(BaseResponse<List<MusicBean>> listBaseResponse) {
                if (getView() != null) {
                    getView().actionSuccess("getMusics", listBaseResponse);
                }
            }
        });
    }
}

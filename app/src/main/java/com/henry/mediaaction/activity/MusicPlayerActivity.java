package com.henry.mediaaction.activity;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.henry.audio.NativeLib;
import com.henry.audio.OnErrorListener;
import com.henry.mediaaction.R;
import com.henry.mediaaction.arouter.ARouterPath;
import com.henry.mediaaction.base.BasePresenter;
import com.henry.mediaaction.base.MvpBaseActivity;
import com.henry.mediaaction.bean.MusicBean;
import com.henry.mediaaction.utils.Logger;

import butterknife.BindView;
@Route(path = ARouterPath.ONE_APP_MAIN.MusicPlayerActivity)
public class MusicPlayerActivity extends MvpBaseActivity {
    private NativeLib nativeLib;
    @BindView(R.id.native_test)
    TextView native_test;
    @Autowired(name = "musicItem")
    MusicBean musicBean;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_music_palyer;
    }

    @Override
    protected void initView() {
        setTitle(musicBean.getMusicName());
        nativeLib = new NativeLib();
        OnErrorListener listener=new OnErrorListener() {
            @Override
            public void onError(String errorCode, String errorMsg) {
                Logger.e("FFMPEG_LOG", errorCode + "--" + errorMsg);
            }
        };
        nativeLib.setErrorListener(listener);
        nativeLib.createChildThread();

    }

    @Override
    protected void initData() {

    }
}
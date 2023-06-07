package com.henry.mediaaction.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.henry.mediaaction.R;
import com.henry.mediaaction.arouter.ARouterPath;
import com.henry.mediaaction.base.BasePresenter;
import com.henry.mediaaction.base.MvpBaseActivity;

@Route(path = ARouterPath.ONE_APP_MAIN.VideoActivity)
public class VideoActivity extends MvpBaseActivity {
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        setTitle("视频列表");
    }

    @Override
    protected void initData() {

    }
}
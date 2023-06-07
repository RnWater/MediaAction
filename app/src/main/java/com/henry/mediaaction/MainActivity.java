package com.henry.mediaaction;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.henry.mediaaction.arouter.ARouterPath;
import com.henry.mediaaction.base.BasePresenter;
import com.henry.mediaaction.base.MvpBaseActivity;

import butterknife.OnClick;

@Route(path = ARouterPath.ONE_APP_MAIN.MainActivity)
public class MainActivity extends MvpBaseActivity {

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    @OnClick(R.id.videoJump)
    public void videoJump(){
        ARouter.getInstance().build(ARouterPath.ONE_APP_MAIN.VideoActivity).navigation();
    }
    @OnClick(R.id.musicJump)
    public void musicJump(){
        ARouter.getInstance().build(ARouterPath.ONE_APP_MAIN.MusicActivity).navigation();
    }
}
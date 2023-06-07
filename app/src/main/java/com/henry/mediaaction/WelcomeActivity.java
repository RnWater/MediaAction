package com.henry.mediaaction;

import android.content.Intent;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.henry.mediaaction.arouter.ARouterPath;
import com.henry.mediaaction.base.BaseConstant;
import com.henry.mediaaction.base.BasePresenter;
import com.henry.mediaaction.base.MvpBaseActivity;
import com.henry.mediaaction.cache.MMKVCache;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class WelcomeActivity extends MvpBaseActivity {
    @BindView(R.id.welcome_img)
    ImageView welcome_img;
    private TimerCounter timerCounter;
    private final long delayTime=3000L;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        int height_bar = MMKVCache.getInstance().getInt(BaseConstant.OTHER.STATE_BAR_HEIGHT);
        if (height_bar <= 0) {
            myOnGlobalLayoutListener = new MyOnGlobalLayoutListener(this);
            welcome_img.getViewTreeObserver().addOnGlobalLayoutListener(myOnGlobalLayoutListener);
        }
    }

    @Override
    protected void initData() {
        if (timerCounter == null) {
            timerCounter = new TimerCounter(delayTime, 1000, this);
            timerCounter.start();
        }
    }

    static class TimerCounter extends CountDownTimer {
        private WeakReference<WelcomeActivity> weakReference;

        public TimerCounter(long millisInFuture, long countDownInterval, WelcomeActivity activity) {
            super(millisInFuture, countDownInterval);
            weakReference = new WeakReference<>(activity);
            if (weakReference.get() == null) {
                return;
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (weakReference.get() == null) {
                return;
            }
            weakReference.get().startActDelay();
        }
    }

    //规定最迟3秒
    protected void startActDelay() {
        ARouter.getInstance().build(ARouterPath.ONE_APP_MAIN.MainActivity).navigation();
        finish();
    }

    private MyOnGlobalLayoutListener myOnGlobalLayoutListener;

    static class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private WeakReference<WelcomeActivity> weakReference;

        private MyOnGlobalLayoutListener(WelcomeActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onGlobalLayout() {
            if (weakReference.get() == null) {
                return;
            }
            Rect frame = new Rect();
            weakReference.get().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int height = frame.top;
            MMKVCache.getInstance().putInt(BaseConstant.OTHER.STATE_BAR_HEIGHT, height);
        }
    }
}
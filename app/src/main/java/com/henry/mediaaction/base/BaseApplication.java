package com.henry.mediaaction.base;

import android.app.Application;

import com.alibaba.android.arouter.BuildConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.henry.mediaaction.cache.MMKVCache;
import com.henry.mediaaction.manager.ActivityManagerLifecycle;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {
    private static BaseApplication application;
    private static int activityCount = 0;
    private static boolean isRunInBackground = false;
    public static final List<String> APPLICATION_LIST = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        MMKVCache.getInstance().init(this);
        initApp();
    }

    private void initApp() {
        initARouter();
        ActivityManagerLifecycle activityManagerLifecycle = new ActivityManagerLifecycle();
        registerActivityLifecycleCallbacks(activityManagerLifecycle);
    }

    /**
     * 初始化路由
     */
    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }

    public static BaseApplication getInstance() {
        return application;
    }
}

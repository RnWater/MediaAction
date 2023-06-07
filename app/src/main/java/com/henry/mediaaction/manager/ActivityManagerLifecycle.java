package com.henry.mediaaction.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.henry.mediaaction.base.BaseActivityManager;

public class ActivityManagerLifecycle  implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        BaseActivityManager.getInstance().addActivity(activity);
    }
    @Override
    public void onActivityStarted(Activity activity) {

    }
    @Override
    public void onActivityResumed(Activity activity) {
    }
    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
    @Override
    public void onActivityDestroyed(Activity activity) {
        if (BaseActivityManager.getInstance().haveCurrentActivity(activity.getClass().getSimpleName())) {
            BaseActivityManager.getInstance().removeCurrent(activity);
        }
    }
}

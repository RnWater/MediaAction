package com.henry.mediaaction.base;
import android.app.Activity;
import android.text.TextUtils;
import com.henry.mediaaction.utils.Logger;
import java.util.Iterator;
import java.util.Stack;
public class BaseActivityManager {
     private volatile static BaseActivityManager instance = null;
    private Stack<Activity> activityStack;

    private BaseActivityManager() {
        activityStack = new Stack<>();
    }

    public static void init() {
        if (instance == null) {
            synchronized (BaseActivityManager.class) {
                if (instance == null) {
                    instance = new BaseActivityManager();
                }
            }
        }
    }

    public static BaseActivityManager getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }
    public void printAllActivity(){
        if (activityStack != null) {
            for (int i=0;i<activityStack.size();i++){
                Activity activity = activityStack.get(i);
            }
        }
    }
    public Activity getTopActivity() {
        try {
            if (!activityStack.isEmpty()) {
                return activityStack.peek();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 看看栈中是否有所需的页面
     * @param activityName
     * @return
     */
    public boolean haveCurrentActivity(String activityName) {
        if (TextUtils.isEmpty(activityName)) {
            return false;
        }
        if (activityName.contains("/")) {
            String[] split = activityName.split("/");
            activityName = split[split.length - 1];
        }
        for (int i = activityStack.size()-1; i >= 0; i--) {
            Activity activity= activityStack.get(i);
            if (activity != null) {
                if (activity.getClass().getSimpleName().equals(activityName)) {
                    return true;
                }
            }
        }
        return false;
    }
    //添加Activity
    public void addActivity(Activity activity) {
        if (null == activity)
            return;
        activityStack.push(activity);
    }

    //删除Activity
    public void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
            }
        } catch (Exception e) {

        }
    }
    //删除相同Activity name
    public void finishActivity(String activityName) {
        if (TextUtils.isEmpty(activityName)) {
            return;
        }
        if (activityName.contains("/")) {
            String[] split = activityName.split("/");
            activityName = split[split.length - 1];
        }
        if (!haveCurrentActivity(activityName)) {
            return;
        }
        try {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                Logger.e("我的页面", next.getClass().getSimpleName() + "--" + activityName);
                if (activityName.equals(next.getClass().getSimpleName())) {
                    iterator.remove();
                    next.finish();
                }
            }
        } catch (Exception e) {
            Logger.e("我的页面异常", e.getMessage());
        }
    }
    //删除Activity
    public void removeCurrent(Activity activity) {
        try {
            if (activity != null) {
                activityStack.remove(activity);
            }
        } catch (Exception e) {

        }
    }
    /**
     * 移除指定区域
     * @param endActivityName
     */
    public void finishFromToWant(String endActivityName){
        try {
            if (TextUtils.isEmpty(endActivityName)) {
                return;
            }
            if (endActivityName.contains("/")) {
                String[] split = endActivityName.split("/");
                endActivityName = split[split.length - 1];
            }
            if (!haveCurrentActivity(endActivityName)) {
                return;
            }
            while (!activityStack.empty()) {
                Activity pop = activityStack.pop();
                if (pop == null) {
                    continue;
                }
                if (endActivityName.equals(pop.getClass().getSimpleName())) {
                    pop.finish();
                    break;
                } else {
                    pop.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 移除指定区域
     * @param endActivityName
     */
    public void finishFromToWantWithoutCurrent(String endActivityName){
        try {
            if (TextUtils.isEmpty(endActivityName)) {
                return;
            }
            if (endActivityName.contains("/")) {
                endActivityName = endActivityName.split("/")[endActivityName.split("/").length - 1];
            }
            if (!haveCurrentActivity(endActivityName)) {
                return;
            }
            while (!activityStack.empty()) {
                Activity pop = activityStack.pop();
                if (pop == null) {
                    continue;
                }
                if (endActivityName.equals(pop.getClass().getSimpleName())) {
                    activityStack.add(pop);
                    break;
                } else {
                    pop.finish();
                }
            }
        } catch (Exception e) {
            Logger.e("我的activity",e.getMessage());
            e.printStackTrace();
        }
    }

    //结束所有Activity
    public void finishAllActivity() {
        try {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (null != activity) {
                    iterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishWithoutCurrent(Class fa) {
        try {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (null != activity) {
                    if (!fa.getSimpleName().equals(activity.getClass().getSimpleName())) {
                        iterator.remove();
                        activity.finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishWithoutCurrent(String className) {
        try {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (null != activity) {
                    if (!activity.getClass().getSimpleName().equals(className)) {
                        iterator.remove();
                        activity.finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.henry.mediaaction.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.henry.mediaaction.R;
import com.henry.mediaaction.progress.KProgressHUD;
import com.henry.mediaaction.utils.DisplayUtils;
import com.henry.mediaaction.utils.Logger;
import com.henry.mediaaction.utils.NetUtils;
import com.henry.mediaaction.utils.StatusBarUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class MvpBaseActivity<T extends BasePresenter, V extends BaseViewInter> extends AppCompatActivity {
    /**
     * 提高效率的稀疏数组
     */
    private final SparseArray<View> views = new SparseArray<>();
    public TextView tv_name, tv_menu;
    protected ImageView back_main_bar;
    protected ImageView activity_right_pic;
    protected T presenter;
    protected Unbinder bind;
    private KProgressHUD hud;
    protected boolean fullScroll = false;

    public boolean getFullScroll() {
        return fullScroll;
    }

    protected long intoPageTime = 0L;//进入页面时间
    //指纹解锁调用次数
    View header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getFullScroll()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //状态栏的处理，透明并和页面重叠
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        StatusBarUtils.setLightStatusBar(this, true);
        //设置软键盘在获取焦点的时候不弹出，在点击的时候才弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(255, 255, 255));
        setContentView(initLayout());
        presenter = getPresenter();//尽量早点创建
        if (presenter != null) {
            presenter.attach((V) this);
        }
        back_main_bar = getView(R.id.back_front);
        if (back_main_bar != null) {
            back_main_bar.setOnClickListener(v -> setBackMethod());
        }
        tv_name = getView(R.id.page_title);
        tv_menu = getView(R.id.right_opt);
        ARouter.getInstance().inject(this);
        bind = ButterKnife.bind(this);
        initView();
        initData();
        if (tv_name != null) {
            setToolBar(tv_name, tv_menu);
            setToolBar(tv_name, activity_right_pic);
        }
        header = findViewById(R.id.top_header);
    }

    public TextView getTv_name() {
        return tv_name;
    }

    public TextView getTv_menu() {
        return tv_menu;
    }

    /**
     * 点击返回键操作
     */
    public void setBackMethod() {
        finish();
    }

    private boolean reset;

    protected abstract T getPresenter();

    protected abstract int initLayout();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.deAttach();
        }
        if (bind != null) {
            bind.unbind();
        }
        endLoading();
        super.onDestroy();
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    protected void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    protected void setToolBar(TextView t_name, TextView t_menu) {
    }

    protected void setToolBar(TextView t_name, ImageView activity_right_pic) {
    }
    /**
     * 隐藏标题栏
     */
    @TargetApi(19)
    public void hideTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 隐藏标题栏 全屏展示
     */
    protected void clearTop() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 显示标题栏
     */
    protected void showTop() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            try {
                getResources();
            } catch (Exception e) {
                e.printStackTrace();
            }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        try {
            if (res.getConfiguration().fontScale != 1) {
                Configuration newConfig = new Configuration();
                newConfig.fontScale = 1.0f;
                DisplayMetrics displayMetrics = res.getDisplayMetrics();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    displayMetrics.setToDefaults();
                    if (displayMetrics.densityDpi < DisplayMetrics.DENSITY_DEVICE_STABLE * 0.92) {
                        displayMetrics.densityDpi = (int) (DisplayMetrics.DENSITY_DEVICE_STABLE * 0.92);
                    } else if (displayMetrics.densityDpi <= DisplayMetrics.DENSITY_DEVICE_STABLE) {
                        displayMetrics.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
                    }
                    newConfig.densityDpi = displayMetrics.densityDpi;
                }
                Context con = getBaseContext().createConfigurationContext(newConfig);
                res = con.getResources();
            }
        } catch (Exception e) {
            Logger.e("MvpBaseActivity", e.getMessage());
        }
        return res;
    }
    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    /**
     * 返回一个具体的view对象
     * 这个就是借鉴的base-adapter-helper中的方法
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    @Override
    protected void onStart() {
        super.onStart();
        fitCutoutHeaderToPixel();
    }

    /**
     * 适配android10 pixel刘海屏
     */
    public void fitCutoutHeaderToPixel() {
        //特殊适配
        String brand = "Pixel 3 XL";
        if (Build.MODEL.contains(brand) && Build.VERSION.SDK_INT >= 29) {
            try {
                if (header != null) {
                    int[] location = new int[2];
                    header.getLocationInWindow(location);
                    int height = DisplayUtils.dipToPx(this, 88);
                    ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
                    layoutParams.height = height;
                    header.setLayoutParams(layoutParams);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示dialog
     */
    public void showLoading() {
        //当activity消失或者finish时候show了Dialog会崩溃
        //时间原因先try  catch 后续需要优化
        try {
            if (NetUtils.isNetAvailable()) {
                if (hud == null) {
                    hud = KProgressHUD.create(this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(true);
                    hud.show();
                } else {
                    if (!hud.isShowing()) {
                        hud.show();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示dialog
     */
    public void showLoading(boolean cancel) {
        //当activity消失或者finish时候show了Dialog会崩溃
        //时间原因先try  catch 后续需要优化
        try {
            if (NetUtils.isNetAvailable()) {
                if (hud == null) {
                    hud = KProgressHUD.create(this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(cancel);
                    hud.setLabel("加载中...");
                    hud.show();
                } else {
                    if (!hud.isShowing()) {
                        hud.show();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示dialog
     */
    public void endLoading() {
        if (hud != null) {
            hud.dismiss();
            hud = null;
        }
    }

    @Override
    public void setTitle(int id) { //设置标题
        TextView textView = (TextView) findViewById(R.id.page_title);
        if (textView != null) {
            textView.setText(id);
        }
        super.setTitle(id);
    }

    public void setTitle(String id) { //设置标题
        TextView textView = (TextView) findViewById(R.id.page_title);
        if (textView != null) {
            textView.setText(id);
        } else {
            textView = findViewById(R.id.page_title);
            if (textView != null) {
                textView.setText(id);
            }
        }
        super.setTitle(id);
    }


    public void onBack(View v) {
        finish();
    }
    @Override
    public void finish() {
        BaseActivityManager.getInstance().removeCurrent(this);
        super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setBackMethod();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

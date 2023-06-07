package com.henry.mediaaction.view;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.henry.mediaaction.R;
import com.henry.mediaaction.base.BaseActivityManager;
import com.henry.mediaaction.base.BaseConstant;
import com.henry.mediaaction.cache.MMKVCache;
import com.henry.mediaaction.utils.StatusBarUtils;
public class StateBar extends View {
    private int height=72;
    private int backColor;
    private boolean needDark;
    public StateBar(Context context) {
        this(context,null);
    }

    public StateBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StateBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateBar, 0, 0);
        backColor=ta.getInteger(R.styleable.StateBar_state_bar_back_color,0xffffffff);
        needDark=ta.getBoolean(R.styleable.StateBar_state_bar_need_dark,false);
        init();
    }
    private void init() {
        height= MMKVCache.getInstance().getInt(BaseConstant.OTHER.STATE_BAR_HEIGHT);
        if (needDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Activity activity = BaseActivityManager.getInstance().getTopActivity();
                StatusBarUtils.setLightStatusBar(activity,true);
            }
        }
        setBackgroundColor(backColor);
    }
    public void againSet(int height){
        this.height = height;
        if (needDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Activity activity = BaseActivityManager.getInstance().getTopActivity();
                StatusBarUtils.setLightStatusBar(activity,true);
            }
        }
        setBackgroundColor(backColor);
        requestLayout();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getWidth(), height);
    }
}

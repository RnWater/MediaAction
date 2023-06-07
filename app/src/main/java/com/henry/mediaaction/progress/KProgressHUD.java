package com.henry.mediaaction.progress;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.henry.mediaaction.R;
import com.henry.mediaaction.utils.DisplayUtils;

public class KProgressHUD {

    public enum Style {
        SPIN_INDETERMINATE, PIE_DETERMINATE, ANNULAR_DETERMINATE, BAR_DETERMINATE
    }

    // To avoid redundant APIs, make the HUD as a wrapper class around a Dialog
    private ProgressDialog mProgressDialog;
    private float mDimAmount;
    private int mWindowColor;
    private float mCornerRadius;
    private Context mContext;

    private int mAnimateSpeed;

    private int mMaxProgress;
    private boolean mIsAutoDismiss;

    private int mGraceTimeMs;
    private Handler mGraceTimer;
    private boolean mFinished;

    public KProgressHUD(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
        mDimAmount = 0;
        //noinspection deprecation
        mWindowColor = context.getResources().getColor(android.R.color.black);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;
        mGraceTimeMs = 0;
        mFinished = false;

        setStyle(Style.PIE_DETERMINATE);
    }
    public KProgressHUD(Context context,boolean showBackground) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context,showBackground);
        mDimAmount = 0;
        //noinspection deprecation
        mWindowColor = context.getResources().getColor(android.R.color.transparent);
        mAnimateSpeed = 1;
        mCornerRadius = 10;
        mIsAutoDismiss = true;
        mGraceTimeMs = 0;
        mFinished = false;

        setStyle(Style.PIE_DETERMINATE);
    }

    /**
     * Create a new HUD. Have the same effect as the constructor.
     * For convenient only.
     * @param context Activity context that the HUD bound to
     * @return An unique HUD instance
     */
    public static KProgressHUD create(Context context) {
        return new KProgressHUD(context);
    }

    public static KProgressHUD create(Context context,boolean showBackground) {
        return new KProgressHUD(context,showBackground);
    }
  /**
   * Create a new HUD. specify the HUD style (if you use a custom view, you need {@code KProgressHUD.create(Context context)}).
   *
   * @param context Activity context that the HUD bound to
   * @param style One of the KProgressHUD.Style values
   * @return An unique HUD instance
   */
    public static KProgressHUD create(Context context, Style style) {
        return new KProgressHUD(context).setStyle(style);
    }

    /**
     * Specify the HUD style (not needed if you use a custom view)
     * @param style One of the KProgressHUD.Style values
     * @return Current HUD
     */
    public KProgressHUD setStyle(Style style) {
        View view = null;
        switch (style) {
            case SPIN_INDETERMINATE:
                view = new LoadingView(mContext);
                break;
            case PIE_DETERMINATE:
                view = new PieView(mContext);
                break;
            case ANNULAR_DETERMINATE:
                view = new AnnularView(mContext);
                break;
            case BAR_DETERMINATE:
                view = new BarView(mContext);
                break;
            // No custom view style here, because view will be added later
        }
        mProgressDialog.setView(view);
        return this;
    }

    /**
     * Specify the dim area around the HUD, like in Dialog
     * @param dimAmount May take value from 0 to 1. Default to 0 (no dimming)
     * @return Current HUD
     */
    public KProgressHUD setDimAmount(float dimAmount) {
        if (dimAmount >= 0 && dimAmount <= 1) {
            mDimAmount = dimAmount;
        }
        return this;
    }

    /**
     * Set HUD size. If not the HUD view will use WRAP_CONTENT instead
     * @param width in dp
     * @param height in dp
     * @return Current HUD
     */
    public KProgressHUD setSize(int width, int height) {
        mProgressDialog.setSize(width, height);
        return this;
    }

    /**
     * @deprecated  As of release 1.1.0, replaced by {@link #setBackgroundColor(int)}
     * @param color ARGB color
     * @return Current HUD
     */
    @Deprecated
    public KProgressHUD setWindowColor(int color) {
        mWindowColor = color;
        return this;
    }

    /**
     * Specify the HUD background color
     * @param color ARGB color
     * @return Current HUD
     */
    public KProgressHUD setBackgroundColor(int color) {
        mWindowColor = color;
        return this;
    }

    /**
     * Specify corner radius of the HUD (default is 10)
     * @param radius Corner radius in dp
     * @return Current HUD
     */
    public KProgressHUD setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }

    /**
     * Change animation speed relative to default. Used with indeterminate style
     * @param scale Default is 1. If you want double the speed, set the param at 2.
     * @return Current HUD
     */
    public KProgressHUD setAnimationSpeed(int scale) {
        mAnimateSpeed = scale;
        return this;
    }

    /**
     * Optional label to be displayed.
     * @return Current HUD
     */
    public KProgressHUD setLabel(String label) {
        mProgressDialog.setLabel(label);
        return this;
    }

    /**
     * Optional label to be displayed
     * @return Current HUD
     */
    public KProgressHUD setLabel(String label, int color) {
        mProgressDialog.setLabel(label, color);
        return this;
    }

    /**
     * Optional detail description to be displayed on the HUD
     * @return Current HUD
     */
    public KProgressHUD setDetailsLabel(String detailsLabel) {
        mProgressDialog.setDetailsLabel(detailsLabel);
        return this;
    }

    /**
     * Optional detail description to be displayed
     * @return Current HUD
     */
    public KProgressHUD setDetailsLabel(String detailsLabel, int color) {
        mProgressDialog.setDetailsLabel(detailsLabel, color);
        return this;
    }

    /**
     * Max value for use in one of the determinate styles
     * @return Current HUD
     */
    public KProgressHUD setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        return this;
    }

    /**
     * Set current progress. Only have effect when use with a determinate style, or a custom
     * view which implements Determinate interface.
     */
    public void setProgress(int progress) {
        mProgressDialog.setProgress(progress);
    }

    /**
     * Provide a custom view to be displayed.
     * @param view Must not be null
     * @return Current HUD
     */
    public KProgressHUD setCustomView(View view) {
        if (view != null) {
            mProgressDialog.setView(view);
        } else {
            throw new RuntimeException("Custom view must not be null!");
        }
        return this;
    }

    /**
     * Specify whether this HUD can be cancelled by using back button (default is false)
     * @return Current HUD
     */
    public KProgressHUD setCancellable(boolean isCancellable) {
        mProgressDialog.setCancelable(isCancellable);
        return this;
    }

    /**
     * Specify whether this HUD closes itself if progress reaches max. Default is true.
     * @return Current HUD
     */
    public KProgressHUD setAutoDismiss(boolean isAutoDismiss) {
        mIsAutoDismiss = isAutoDismiss;
        return this;
    }

    /**
     * Grace period is the time (in milliseconds) that the invoked method may be run without
     * showing the HUD. If the task finishes before the grace time runs out, the HUD will
     * not be shown at all.
     * This may be used to prevent HUD display for very short tasks.
     * Defaults to 0 (no grace time).
     * @param graceTimeMs Grace time in milliseconds
     * @return Current HUD
     */
    public KProgressHUD setGraceTime(int graceTimeMs) {
        mGraceTimeMs = graceTimeMs;
        return this;
    }

    public KProgressHUD show() {
            mFinished = false;
            if (mGraceTimeMs == 0) {
                mProgressDialog.show();
            } else {
                mGraceTimer = new Handler();
                mGraceTimer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null && !mFinished) {
                            mProgressDialog.show();
                        }
                    }
                }, mGraceTimeMs);
            }
        return this;
    }

    public boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void dismiss() {
        mFinished = true;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (mGraceTimer != null) {
            mGraceTimer.removeCallbacksAndMessages(null);
            mGraceTimer = null;
        }
    }

    private class ProgressDialog extends Dialog {

        private Determinate mDeterminateView;
        private Indeterminate mIndeterminateView;
        private View mView;
        private TextView showText;
        private FrameLayout mCustomViewContainer;
        private BackgroundLayout mBackgroundLayout;
        private int mWidth, mHeight;

        boolean showBackground = true;//增加是否显示对话框的黑色背景，默认是显示黑色背景
        public ProgressDialog(Context context) {
            super(context,R.style.permission_dialog);
        }

        public ProgressDialog(Context context,boolean showBackground) {
            super(context);
            this. showBackground = showBackground;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = getWindow();
            setContentView(R.layout.kprogresshud_hud);
            if (showBackground) {
                clearBackground(false);
            }
            WindowManager m = getWindow().getWindowManager();
            Display d = m.getDefaultDisplay();
            //为获取屏幕宽、高
            WindowManager.LayoutParams p = this.getWindow().getAttributes(); //获取对话框当前的参数值
            Point outSize = new Point();
            d.getRealSize(outSize);
            p.height = outSize.y;
            p.width = outSize.x;
            this.getWindow().setAttributes(p); //设置生效
            p.gravity = Gravity.CENTER;
            window.setAttributes(p);

            setCanceledOnTouchOutside(false);

            initViews();
        }

        /**
         * 设置展示的提示
         * @param text
         */
        public void setShowText(String text){
            if (showText != null&& !TextUtils.isEmpty(text)) {
                showText.setVisibility(View.VISIBLE);
                showText.setText(text);
            }
        }
        /**
         * 清除背景色
         * @param clear
         */
        public void clearBackground(boolean clear){
            if (clear) {
                getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  //清除灰色背景
            }
        }
        @Override
        public void show() {
            super.show();
            if (mView == null) {
                return;
            }
            if (mView instanceof LoadingView) {
                ((LoadingView) mView).start();
            }
        }

        @Override
        public void dismiss() {
            super.dismiss();
            if (mView == null) {
                return;
            }
            if (mView instanceof LoadingView) {
                ((LoadingView) mView).stop();
            }
        }

        private void initViews() {
            mBackgroundLayout =  findViewById(R.id.background);
            mBackgroundLayout.setBaseColor(mWindowColor);
            mBackgroundLayout.setCornerRadius(mCornerRadius);
            int width = DisplayUtils.getScreenWidthPixels(getContext());
            int height=DisplayUtils.getScreenHeightPixels(getContext());
            width = Math.min(width, height);
            mWidth = (int) (width/3.8f);
            mHeight = (int) (width/3.8f);
            if (mWidth != 0) {
                updateBackgroundSize();
            }
            mCustomViewContainer =  findViewById(R.id.container);
            addViewToFrame(mView);
            if (mDeterminateView != null) {
                mDeterminateView.setMax(mMaxProgress);
            }
            if (mIndeterminateView != null) {
                mIndeterminateView.setAnimationSpeed(mAnimateSpeed);
            }
            showText =  findViewById(R.id.showText);
        }

        private void addViewToFrame(View view) {
            if (view == null) {
                return;
            }
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)(mWidth*0.6), (int)(mWidth*0.6));
            mCustomViewContainer.addView(view, params);
        }

        private void updateBackgroundSize() {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBackgroundLayout.getLayoutParams();
            params.width = mWidth;
            params.height = mHeight;
            mBackgroundLayout.setLayoutParams(params);
        }

        public void setProgress(int progress) {
            if (mDeterminateView != null) {
                mDeterminateView.setProgress(progress);
                if (mIsAutoDismiss && progress >= mMaxProgress) {
                    dismiss();
                }
            }
        }

        public void setView(View view) {
            if (view != null) {
                if (view instanceof Determinate) {
                    mDeterminateView = (Determinate) view;
                }
                if (view instanceof Indeterminate) {
                    mIndeterminateView = (Indeterminate) view;
                }
                mView = view;
                if (isShowing()) {
                    mCustomViewContainer.removeAllViews();
                    addViewToFrame(view);
                }
            }
        }

        public void setLabel(String label) {
            setShowText(label);
        }

        public void setDetailsLabel(String detailsLabel) {
        }

        public void setLabel(String label, int color) {
//            mLabel = label;
//            mLabelColor = color;
//            if (mLabelText != null) {
//                if (label != null) {
//                    mLabelText.setText(label);
//                    mLabelText.setTextColor(color);
//                    mLabelText.setVisibility(View.VISIBLE);
//                } else {
//                    mLabelText.setVisibility(View.GONE);
//                }
//            }
        }

        public void setDetailsLabel(String detailsLabel, int color) {
//            mDetailsLabel = detailsLabel;
//            mDetailColor = color;
//            if (mDetailsText != null) {
//                if (detailsLabel != null) {
//                    mDetailsText.setText(detailsLabel);
//                    mDetailsText.setTextColor(color);
//                    mDetailsText.setVisibility(View.VISIBLE);
//                } else {
//                    mDetailsText.setVisibility(View.GONE);
//                }
//            }
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
            if (mBackgroundLayout != null) {
                updateBackgroundSize();
            }
        }
    }
}

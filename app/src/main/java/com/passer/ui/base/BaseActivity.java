package com.passer.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.passer.R;
import com.passer.util.DialogUtil;
import com.passer.util.LogUtils;


/***
 * Activity 的父类，主要用于写一些可能用上的方法
 */
public abstract class BaseActivity extends AppCompatActivity {
    public FrameLayout rootContainer;
    public Context context;
    private FrameLayout refreshLayout;
    protected String TAG;
    private ProgressDialog dialog;//进度窗口，使当前界面不可点击

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 禁止横屏，所有继承该Activity的之类都不支持横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        rootContainer = (FrameLayout) this.findViewById(android.R.id.content);
        setContentView(getLayoutId());
        initViews();
        setupView(savedInstanceState);
        if (getIntent() != null) handleIntent(getIntent());
        TAG = getClass().getSimpleName();
        LogUtils.e(TAG, "启动");
    }

    //处理intent信息
    public void handleIntent(Intent intent) {
    }


    public abstract int getLayoutId();

    public abstract void initViews();

    public abstract void setupView(Bundle bundle);

    public void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    /**
     * 显示提醒加载的dialog
     *
     * @param title
     * @param message
     */
    public void showProgressDialog(String title, String message) {
        dialog = DialogUtil.createProgressDialog(context, title, message);
        dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog != null) dialog.cancel();
    }

    /**
     * 与setContentView()方法对应
     *
     * @return
     */
    public View getContentView() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        return view.getChildAt(0);
    }

    /**
     * 在屏幕上添加一个转动的ProgressBar，默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则progressbar将会处于最底层，被屏幕其他View给覆盖
     *
     * @param customIndeterminateDrawable 自定义的样式，可以为null，此时为系统默认
     * @return {ProgressBar}
     */
    public ProgressBar createProgressBar(Context context, Drawable customIndeterminateDrawable, int defStyleAttr) {
        // 给progressbar准备一个FrameLayout的LayoutParams
        rootContainer = (FrameLayout) this.findViewById(android.R.id.content);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        ProgressBar progressBar ;
        if (defStyleAttr == 0) {
            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        } else {
            progressBar = new ProgressBar(context, null, defStyleAttr);
        }
        progressBar.setLayoutParams(lp);
        // 自定义progressBar的Drawable
        if (customIndeterminateDrawable != null) {
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将progressBar添加到FrameLayout中
        rootContainer.addView(progressBar);
        return progressBar;
    }

    /**
     * 显示正在加载界面
     *
     * @param context
     * @return
     */
    public View createLoadingView(Context context) {
        rootContainer = (FrameLayout) this.findViewById(android.R.id.content);
        View view = View.inflate(context, R.layout.include_loading_layout, null);
        rootContainer.addView(view);
        return view;
    }

    /**
     * 重新加载回调
     *
     * @param listener
     */
    public void showReLoadView(final OnReloadListener listener) {
        rootContainer = (FrameLayout) this.findViewById(android.R.id.content);
        final View view = View.inflate(context, R.layout.include_refresh_layout, null);
        rootContainer.addView(view);
        TextView tv = (TextView) view.findViewById(R.id.tv_onreload);
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onReload();
                rootContainer.removeView(view);
            }
        });
    }

    /**
     * 重新加载的时候回调。
     *
     * @author huanghuan
     */
    public interface OnReloadListener {
        void onReload();
    }


    public void showMessageDialog(String message) {
        DialogUtil.createMessageDialog(context, "提醒", message, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}

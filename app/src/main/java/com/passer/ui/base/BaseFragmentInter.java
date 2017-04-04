package com.passer.ui.base;

import android.support.v4.app.Fragment;

/**
 * Created by ZCYL on 2017/3/19.
 * Fragment的抽象接口
 */

public interface BaseFragmentInter {
    void showToast(String msg);
    //显示或者隐藏加载dialog
    void showProgressBar();
    void hideProgressBar();

    /**
     * 数据加载失败后显示的重加载按钮
     * @param listener 监听器
     */
    void showReLoadView(BaseActivity.OnReloadListener listener);

    //显示或者隐藏加载进度dialog
    void showProgressDialog(String title, String message);
    void hideProgressDialog();

    /**
     * 更换fragment
     * @param target 目标fragment
     */
    void translateFragment(Fragment target);
}

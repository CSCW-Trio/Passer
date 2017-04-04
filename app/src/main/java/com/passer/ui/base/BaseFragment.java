package com.passer.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.passer.util.LogUtils;


/**
 * Created by acer-PC on 2016/10/20.
 * 采用延迟加载技术，可选是否实现延迟加载
 */

public abstract class BaseFragment extends Fragment implements BaseFragmentInter {

    protected AppCompatActivity mParentActivity;
    protected String TAG = getClass().getSimpleName();
    ProgressBar mProgressBar;
    ProgressDialog dialog; //进度窗口，会使当前界面不可点击
    View view;
    //获取布局id和初始化
    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        this.view = view;
        this.mParentActivity = (AppCompatActivity) getActivity();
        //只处理findview，数据绑定由lazyload完成
        initView(view, savedInstanceState);
        LogUtils.e(TAG,"启动");
        return view;
    }


    @Override
    public void onAttach(Context context) {
        mParentActivity = (AppCompatActivity) context;
        super.onAttach(context);
    }


    //更换fragment
    public void translateFragment(Fragment target){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(getId(),target,target.getClass().getSimpleName());
        transaction.hide(this);
        transaction.show(target);
        transaction.addToBackStack(this.getClass().getSimpleName());
        transaction.commit();
    }

    public void showProgressBar() {
        mProgressBar = mProgressBar == null ?
                ((BaseActivity)mParentActivity).createProgressBar(mParentActivity,null,0) : mProgressBar;
        mProgressBar.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar() {
        if (mProgressBar == null) return;
        mProgressBar.setVisibility(View.GONE);
    }

    public void showProgressDialog(String title,String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(mParentActivity);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(false);
        }

        dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog != null) dialog.dismiss();
    }

    public void showToast(String msg) {

        Toast.makeText(mParentActivity,msg,Toast.LENGTH_SHORT).show();
    }

    public void showReLoadView(BaseActivity.OnReloadListener listener) {
        ((BaseActivity) mParentActivity).showReLoadView(listener);
    }


}

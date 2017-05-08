package com.passer.ui.young;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by 47420 on 2017/5/8.
 */

public class LoginPre implements ILoginPre {

    private ILoginView mILoginView;
    private Handler mHandler;


    public LoginPre(ILoginView ILoginView) {
        mILoginView = ILoginView;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void clear(){
        mILoginView.onClearText();
    }

    @Override
    public void login(String name, String password) {
        if (name.equals("admin")&&password.equals("123456")){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mILoginView.onLoginResult(true,100);
                }
            },2000);
        }
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        mILoginView.onSetProgressBarVisibility(visibility);
    }

    @Override
    public void doStatActivity() {
        mILoginView.OnStartActivity();
    }


}

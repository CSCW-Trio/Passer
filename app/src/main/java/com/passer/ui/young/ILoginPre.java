package com.passer.ui.young;

/**
 * Created by 47420 on 2017/5/8.
 */

public interface ILoginPre {
    public void clear();//清楚账号密码
    public void login(String name,String password);//登录
    public void setProgressBarVisibility(int visibility);//加载框可见性
    void doStatActivity();

}

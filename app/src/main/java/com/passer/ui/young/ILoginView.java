package com.passer.ui.young;

import android.content.Context;

/**
 * Created by kaede on 2015/5/18.
 */
public interface ILoginView {
	public void onClearText();
	public void onLoginResult(Boolean result, int code);
	public void onSetProgressBarVisibility(int visibility);
    void OnStartActivity();
}

package com.passer.ui.young;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.passer.PasserActivity;
import com.passer.R;
import com.passer.ui.base.BaseActivity;

/**
 * Created by 47420 on 2017/4/5.
 */

public class LoginActivity extends BaseActivity implements ILoginView,View.OnClickListener{
    private EditText et_count,et_code;
    private Button btn_login;
    private LoginPre mLoginPre;
    private ProgressBar mProgressBar;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        et_count = (EditText) findViewById(R.id.et_mycount);
        et_code = (EditText) findViewById(R.id.et_mycode);
        btn_login = (Button) findViewById(R.id.btn_login);
        mLoginPre = new LoginPre(this);
        btn_login.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_login);
        mLoginPre.setProgressBarVisibility(View.INVISIBLE);
    }

    @Override
    public void setupView(Bundle bundle) {

    }

    @Override
    public void onClearText() {
        et_count.setText("");
        et_code.setText("");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        mLoginPre.setProgressBarVisibility(View.INVISIBLE);

        if (result){
            Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show();
            mLoginPre.doStatActivity();
        }
        else
            Toast.makeText(this,"Login Fail, code = " + code,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    @Override
    public void OnStartActivity() {
        startActivity(new Intent(this,PasserActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                mLoginPre.setProgressBarVisibility(View.VISIBLE);
                mLoginPre.login(et_count.getText().toString(),et_code.getText().toString());
                break;
            default:
                break;
        }
    }
}

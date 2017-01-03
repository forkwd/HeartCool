package com.uteamtec.heartcool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.uteamtec.heartcool.R;
import com.uteamtec.heartcool.service.type.Config;
import com.uteamtec.heartcool.service.type.GlobalVar;

/**
 * 欢迎页面
 * Created by wd
 */
public class AeroCardioWelcomeActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (false) { // 测试
            gotoNetTest();
            return;
        }

        setContentView(R.layout.activity_aerocardio_welcome);
    }

    @Override
    protected void initViews() {
        // 默认是第一次进入应用
//                if (Config.getBoolean(Config.Info, Config.PREF_APP_FIRST, true)) {
//                }
        Config.putBoolean(Config.Info, Config.PREF_APP_FIRST, false);

        // 设置是否自动登录
        Config.putBoolean(Config.Info, Config.PREF_LOGIN_AUTO,
                !TextUtils.isEmpty(GlobalVar.getUser().getPassword()));

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                gotoLogin();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected boolean enableBackPressedFinish() {
        return false;
    }

    @Override
    protected boolean enableServiceConnection() {
        return false;
    }

    @Override
    public void onServiceConnected() {
    }

    @Override
    public void onServiceDisconnected() {
    }

    private void gotoNetTest() {
        startActivity(new Intent(this, TestAppNetActivity.class));
        finish();
    }

    private void gotoLogin() {
        startActivity(new Intent(this, AeroCardioLoginActivity.class));
        finish();
    }

}

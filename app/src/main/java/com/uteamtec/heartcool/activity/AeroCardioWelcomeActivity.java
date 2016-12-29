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
        setContentView(R.layout.activity_aerocardio_welcome);

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Intent intent;
                // 默认是第一次进入应用
                if (Config.getBoolean(Config.Info, Config.PREF_APP_FIRST, true)) {
                    intent = new Intent(AeroCardioWelcomeActivity.this, AeroCardioLoginActivity.class);
                } else {
                    intent = new Intent(AeroCardioWelcomeActivity.this, AeroCardioLoginActivity.class);
//                    intent = new Intent(AeroCardioWelcomeActivity.this, TestAppNetActivity.class);
//                intent = new Intent(AeroCardioWelcomeActivity.this, TestDBActivity.class);
//                intent = new Intent(AeroCardioWelcomeActivity.this, TestHistoryActivity.class);
                }

                Config.putBoolean(Config.Info, Config.PREF_APP_FIRST, false);
                Config.putBoolean(Config.Info, Config.PREF_LOGIN_AUTO,
                        !TextUtils.isEmpty(GlobalVar.getUser().getPassword()));

                startActivity(intent);
                AeroCardioWelcomeActivity.this.finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void initViews() {
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

}

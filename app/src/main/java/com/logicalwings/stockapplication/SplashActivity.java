package com.logicalwings.stockapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.logicalwings.stockapplication.base.BaseActivity;
import com.logicalwings.stockapplication.utils.PrefrenceConnector;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initData();

        initUi();
    }

    @Override
    public void initData() {
        super.initData();
        if (PrefrenceConnector.readBoolean(SplashActivity.this, PrefrenceConnector.IS_USER_LOGIN, false)) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {Intent intent = new Intent(SplashActivity.this, SearchListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
                }
            },5000);
        }else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            },5000);
        }
    }

    @Override
    public void initUi() {
        super.initUi();
    }

}


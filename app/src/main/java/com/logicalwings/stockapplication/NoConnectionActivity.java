package com.logicalwings.stockapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.logicalwings.stockapplication.base.BaseActivity;
import com.logicalwings.stockapplication.utils.AppUtils;
import com.logicalwings.stockapplication.utils.Toaster;

public class NoConnectionActivity extends BaseActivity {

    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        initData();

        initUi();
    }


    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initUi() {
        super.initUi();
        findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppUtils.isOnline(NoConnectionActivity.this)) {
                    Intent intent = new Intent(NoConnectionActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {

                    Toaster.popInternetUnavailableToast(NoConnectionActivity.this);
                }

            }
        });
    }
}

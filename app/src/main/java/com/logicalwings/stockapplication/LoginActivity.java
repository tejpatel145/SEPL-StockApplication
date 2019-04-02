package com.logicalwings.stockapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.session.MediaSession;
import android.net.ConnectivityManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.logicalwings.stockapplication.api.ApiClient;
import com.logicalwings.stockapplication.api.ApiInterFace;
import com.logicalwings.stockapplication.base.BaseActivity;
import com.logicalwings.stockapplication.model.TokenModel;
import com.logicalwings.stockapplication.utils.AppConstance;
import com.logicalwings.stockapplication.utils.PrefrenceConnector;
import com.logicalwings.stockapplication.utils.Toaster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    private TextInputLayout input_layout_email;
    private TextInputLayout input_layout_password;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        receiver = new NetworkChangeReceiver();

        registerReceiver(receiver, filter);

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.login);

        input_layout_email = findViewById(R.id.input_layout_email);
        input_layout_password = findViewById(R.id.input_layout_password);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);


        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */
                    hideKeyboard();
                    loginUser();

                    handled = true;
                }
                return handled;
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    input_layout_email.setError("Enter Username");
                    input_layout_password.setError(null);
                } else {
                    input_layout_email.setError(null);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    input_layout_password.setError("Enter Password");
                    input_layout_email.setError(null);
                } else {
                    input_layout_password.setError(null);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (performValidation()) {
                    hideKeyboard();
                    loginUser();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        errorNull();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean performValidation() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        boolean flag = true;

        input_layout_email.setError(null);
        input_layout_password.setError(null);

        if (email.trim().length() == 0) {

            etEmail.requestFocus();
            input_layout_email.setError("Enter Username");
            flag = false;
        } else {
            input_layout_email.setError(null);
            etPassword.requestFocus();
        }

        if (!flag)
            return false;

        if (password.trim().length() == 0) {

            etPassword.requestFocus();
            input_layout_password.setError("Enter Password");
            flag = false;
        } else {
            input_layout_password.setError(null);
            etEmail.requestFocus();
        }

        return flag;
    }

    public void loginUser() {
        final TokenModel tokenModel = null;
        
        final String userName = etEmail.getText().toString().trim();

        final String password = etPassword.getText().toString().trim();

        showProgressDialog(getString(R.string.please_wait));

        ApiInterFace service = ApiClient.getClient(LoginActivity.this);

        service.loginUser("password", userName, password, "1")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TokenModel>() {

                    @Override
                    public void onCompleted() {
                        if (AppConstance.DEBUG)
                            Log.i(AppConstance.DEBUG_TAG, "onCompleted: ");

                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (AppConstance.DEBUG)
                            Log.i(AppConstance.DEBUG_TAG, "onError: "+ e.getMessage()+"Code :"+((HttpException) e).code());
                        if (((HttpException) e).code() == AppConstance.LOGIN_BAD_RESPONSE_CODE) {
                            Toaster.popShortToast(LoginActivity.this,"Invalid UserName or Password.");
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(TokenModel tokenModel) {

                        if (AppConstance.DEBUG)
                            Log.i(AppConstance.DEBUG_TAG, "onNext: ");

                        if (tokenModel != null) {

                            PrefrenceConnector.writeString(LoginActivity.this, PrefrenceConnector.USER_TOKEN, tokenModel.mAccessToken);

                            PrefrenceConnector.writeString(LoginActivity.this, PrefrenceConnector.EMAIL, userName);

                            PrefrenceConnector.writeString(LoginActivity.this, PrefrenceConnector.PASSWORD, password);

                            PrefrenceConnector.writeBoolean(LoginActivity.this, PrefrenceConnector.IS_USER_LOGIN, true);

                            PrefrenceConnector.writeString(LoginActivity.this,PrefrenceConnector.SELECTED_STOCK_TYPE,"inprocess");

                            Toaster.popShortToast(LoginActivity.this, "login Successfully.");

                            Intent intent = new Intent(LoginActivity.this, SearchListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }

                        hideProgressDialog();
                    }
                });

    }

    /**
     * use to hide keyBoard
     */
    private void hideKeyboard() {

        View view = LoginActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void errorNull() {
        input_layout_email.setError(null);
        input_layout_password.setError(null);
    }
}

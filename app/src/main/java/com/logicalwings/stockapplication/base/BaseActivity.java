package com.logicalwings.stockapplication.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.logicalwings.stockapplication.NoConnectionActivity;
import com.logicalwings.stockapplication.api.ApiClient;
import com.logicalwings.stockapplication.api.ApiInterFace;
import com.logicalwings.stockapplication.model.TokenModel;
import com.logicalwings.stockapplication.utils.AppConstance;
import com.logicalwings.stockapplication.utils.AppData;
import com.logicalwings.stockapplication.utils.AppStatus;
import com.logicalwings.stockapplication.utils.PrefrenceConnector;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;
    public String userName;
    public String password;
    public NetworkChangeReceiver receiver;
    protected AppData appData;

    public void initData() {
        appData = AppData.getInstance();

    }

    public void initUi() {

        progressDialog = progressDialog = new ProgressDialog(this);
    }

    public void showProgressDialog(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {

            String status = AppStatus.getInstance(context).getConnectivityStatusString(context);

            if (status.equalsIgnoreCase("3")) {
                Intent intent1 = new Intent(BaseActivity.this, NoConnectionActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }

        }
    }
        protected void onTokenRefreshSuccess(int code) {
        }

        protected void onTokenRefreshFail(int code) {
        }

        protected void onTokenRefreshBadResponse(int code) {
            Intent intent = new Intent(AppConstance.AUTHENTICATION_CHANGE_BROADCAST);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        public void refreshToken(final int code) {

//            Log.d(AppConstance.DEBUG_TAG, "refreshToken() called with: code = [" + code + "]");

            //    showProgressDialog(getString(R.string.please_wait));
            userName = PrefrenceConnector.readString(BaseActivity.this, PrefrenceConnector.EMAIL, "");
            password = PrefrenceConnector.readString(BaseActivity.this, PrefrenceConnector.PASSWORD, "");

            ApiInterFace service = ApiClient.getClient(BaseActivity.this);

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
                                Log.i(AppConstance.DEBUG_TAG, "onError: " + e.getMessage());
                            onTokenRefreshFail(code);
                            hideProgressDialog();
                        }

                        @Override
                        public void onNext(TokenModel tokenModel) {

                            if (AppConstance.DEBUG)
                                Log.i(AppConstance.DEBUG_TAG, "onNext: ");

                            if (tokenModel != null) {

                                PrefrenceConnector.writeString(BaseActivity.this, PrefrenceConnector.USER_TOKEN, tokenModel.mAccessToken);

                                PrefrenceConnector.writeBoolean(BaseActivity.this, PrefrenceConnector.IS_USER_LOGIN, true);

                                onTokenRefreshSuccess(code);
                            } else {
                                onTokenRefreshBadResponse(code);
                            }
                        }
                    });
        }

}

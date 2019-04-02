package com.logicalwings.stockapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppStatus {

    private static AppStatus instance = new AppStatus();
    static Context context;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static AppStatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = AppStatus.getConnectivityStatus(context);
        String status = null;
        if (conn == AppStatus.TYPE_WIFI) {
            // status = "Wifi enabled";

            status = "1";

        } else if (conn == AppStatus.TYPE_MOBILE) {
            //status = "Mobile data enabled";

            status = "2";

        } else if (conn == AppStatus.TYPE_NOT_CONNECTED) {
            // status = "Not connected to Internet";

            status = "3";

        }
        return status;
    }
}

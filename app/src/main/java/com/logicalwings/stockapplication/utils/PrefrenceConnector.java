package com.logicalwings.stockapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class PrefrenceConnector {
    private static final String PREF_NAME = "STOCKAPP";
    private static final int MODE = Context.MODE_PRIVATE;
    public static final String USER_TOKEN = "user_token";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IS_USER_LOGIN = "is_user_login";
    public static final String SELECTED_STOCK_TYPE= "stock_type";

    private static PrefrenceConnector instance;
    private Context context;


    public PrefrenceConnector(Context context) {
        this.context = context;
    }

    public static PrefrenceConnector getInstance(Context context) {
        if (instance == null) {
            instance = new PrefrenceConnector(context);
        }
        return instance;
    }


    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).apply();
    }

    public static boolean readBoolean(Context context, String key, boolean defaultValue) {
        return getPreferences(context).getBoolean(key, defaultValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).apply();
    }

    public static String readString(Context context, String key, String defaultValue) {
        return getPreferences(context).getString(key, defaultValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).apply();
    }

    public int readInteger(Context context, String key, int defaultValue) {
        return getPreferences(context).getInt(key, defaultValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static void clearAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.remove(PREF_NAME);
        editor.apply();

    }

    public static void clearAtLogout(Context context) {

        new Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(USER_TOKEN);
        editor.remove(IS_USER_LOGIN);
        editor.remove(SELECTED_STOCK_TYPE);
        editor.apply();

    }

}

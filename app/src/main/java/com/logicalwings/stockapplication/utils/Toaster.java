package com.logicalwings.stockapplication.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.logicalwings.stockapplication.R;

public class Toaster {

    public static void popShortToast(Context ctx, String toast_msg) {
        if (ctx == null)
            return;
        try {
            if (!toast_msg.trim().equalsIgnoreCase("")) {
                Toast toast = Toast.makeText(ctx, toast_msg, Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void popInternetUnavailableToast(Context ctx) {
        if (ctx == null)
            return;
        try {
            String toast_msg = ctx.getResources().getString(R.string.toast_internet_unavailable);
            if (!toast_msg.trim().equalsIgnoreCase(""))
                popShortToast(ctx, toast_msg);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}

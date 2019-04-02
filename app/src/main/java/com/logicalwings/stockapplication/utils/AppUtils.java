package com.logicalwings.stockapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.logicalwings.stockapplication.R;
import com.logicalwings.stockapplication.model.AddTypeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppUtils {

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static ArrayList<AddTypeData> createFinishList(Context context)
    {
        ArrayList<AddTypeData> typeList = new ArrayList<>();

        typeList.add(new AddTypeData("",0,"Drum Number",false));
        typeList.add(new AddTypeData("",1,"Size",false));
        typeList.add(new AddTypeData("",2,"Date",false));
        typeList.add(new AddTypeData("",3,"Job Number",false));
        typeList.add(new AddTypeData("",4,"Meter",false));
        typeList.add(new AddTypeData("",5,"Remark",false));
        typeList.add(new AddTypeData("",6,"Comment",false));
        typeList.add(new AddTypeData("",7,"Update",false));

        return typeList;
    }

    public static ArrayList<AddTypeData> createInProcessList(Context context)
    {
        ArrayList<AddTypeData> typeList = new ArrayList<>();

        typeList.add(new AddTypeData("",0,"Drum Number",false));
        typeList.add(new AddTypeData("",1,"Size",false));
        typeList.add(new AddTypeData("",2,"Date",false));
        typeList.add(new AddTypeData("",3,"Job Number",false));
        typeList.add(new AddTypeData("",4,"Meter",false));
        typeList.add(new AddTypeData("",6,"Comment",false));
        typeList.add(new AddTypeData("",8,"Cond Type",false));
        typeList.add(new AddTypeData("",9,"Stage",false));
        typeList.add(new AddTypeData("",10,"Location",false));

        return typeList;
    }
}

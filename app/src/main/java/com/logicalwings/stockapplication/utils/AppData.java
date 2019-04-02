package com.logicalwings.stockapplication.utils;

import android.util.Log;

import com.logicalwings.stockapplication.model.AddTypeData;

import java.util.ArrayList;

public class AppData {

    private ArrayList<AddTypeData> addTypeDatas;
    private static AppData appData;

    public static AppData getInstance() {
        if (appData == null) {
            appData = new AppData();
            appData.addTypeDatas = new ArrayList<>();
        }

        return appData;
    }

    public ArrayList<AddTypeData> getAddTypeDatas() {
        return addTypeDatas;
    }

    public void setAddTypeDatas(ArrayList<AddTypeData> addTypeDatas) {
        this.addTypeDatas = addTypeDatas;
    }

    public AddTypeData getAddType(int key) {

        for (AddTypeData addTypeData : addTypeDatas) {
            if (addTypeData.getKey().equals(key)) {
                return addTypeData;
            }
        }
        return null;
    }
}

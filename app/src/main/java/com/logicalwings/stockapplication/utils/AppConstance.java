package com.logicalwings.stockapplication.utils;

import com.logicalwings.stockapplication.model.ValueIndexBean;


public class AppConstance {

    public static final boolean DEBUG = false;
    public static final String DEBUG_TAG = "STOCK_APP";

    public static final String PREFS_FILE_NAME_PARAM = "stockappPrefFile";

    public static final String DRUM_UPDATE_MODE_KEY = "drum_update";
    public static final String SELECTED_DRUM_KEY = "selected_drum";
    public static final String REQUEST_FILTERTYPE_LIST = "filterList";
    public static final String SELECTED_STATUS_KEY = "statuskey";
    public static final String SELECTED_STOCKTYPE_KEY = "filterkey";
    public static final String AUTHENTICATION_CHANGE_BROADCAST = "authentication_change_broadcast";


    public static final int UPDATE_MODE = 1;
    public static final int FILTER_DATA = 2;


    public static final int LOGIN_TOKEN_EXPIRE_CODE = 401;
    public static final int LOGIN_BAD_RESPONSE_CODE = 400;

    public static final ValueIndexBean[] SEARCH_TYPE = new ValueIndexBean[]{
            new ValueIndexBean(0,"Drum No"),
            new ValueIndexBean( 1,"Size"),
            new ValueIndexBean(2,"Date"),
            new ValueIndexBean(3,"Job No"),
            new ValueIndexBean(4,"Meter"),
            new ValueIndexBean(5,"Remark"),
            new ValueIndexBean(6,"Comment"),
            new ValueIndexBean(7,"Update"),
    };

}

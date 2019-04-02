package com.logicalwings.stockapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSearchData extends BaseModel {

    @SerializedName("Data")
    public SearchData data;

    public SearchData getData() {
        return data;
    }

    public void setData(SearchData data) {
        this.data = data;
    }


}

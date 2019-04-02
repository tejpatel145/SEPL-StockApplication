package com.logicalwings.stockapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterTypeData  implements Parcelable {

    @SerializedName("Key")
    @Expose
    private Integer key;
    @SerializedName("value")
    @Expose
    private String value;

    public FilterTypeData(String value, int key) {
        this.value = value;
        this.key = key;
    }

    public FilterTypeData() {

    }

    protected FilterTypeData(Parcel in) {
        key = in.readByte() == 0x00 ? null : in.readInt();
        value = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (key == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(key);
        }
        dest.writeString(value);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FilterTypeData> CREATOR = new Parcelable.Creator<FilterTypeData>() {
        @Override
        public FilterTypeData createFromParcel(Parcel in) {
            return new FilterTypeData(in);
        }

        @Override
        public FilterTypeData[] newArray(int size) {
            return new FilterTypeData[size];
        }
    };
}

package com.logicalwings.stockapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTypeData implements Parcelable {

    @SerializedName("Key")
    @Expose
    private Integer key;
    @SerializedName("value")
    @Expose
    private String value;

    private String detail;

    private boolean isChecked;

    public AddTypeData(String value, int key, String detail, boolean isChecked) {
        this.value = value;
        this.key = key;
        this.detail = detail;
        this.isChecked = isChecked;
    }

    public AddTypeData(String detail, int key, boolean isChecked) {
        this.detail = detail;
        this.key = key;
        this.isChecked = isChecked;
    }

    public AddTypeData( int key,String detail) {
        this.detail = detail;
        this.key = key;
    }

    public AddTypeData() {

    }

    protected AddTypeData(Parcel in) {
        key = in.readByte() == 0x00 ? null : in.readInt();
        value = in.readString();
        detail = in.readString();
        isChecked = in.readByte() != 0x00;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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
        dest.writeString(detail);
        dest.writeByte((byte) (isChecked ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<AddTypeData> CREATOR = new Creator<AddTypeData>() {
        @Override
        public AddTypeData createFromParcel(Parcel in) {
            return new AddTypeData(in);
        }

        @Override
        public AddTypeData[] newArray(int size) {
            return new AddTypeData[size];
        }
    };
}

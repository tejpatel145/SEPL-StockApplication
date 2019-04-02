package com.logicalwings.stockapplication.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockData implements Parcelable {
    @SerializedName("SrNo")
    @Expose
    private Integer srNo;
    @SerializedName("Month")
    @Expose
    private String month;
    @SerializedName("StockType")
    @Expose
    private String stockType;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("DrumNo")
    @Expose
    private String drumNo;
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("JobNo")
    @Expose
    private String jobNo;
    @SerializedName("QtyMeter")
    @Expose
    private Integer qtyMeter;
    @SerializedName("CondType")
    @Expose
    private String condType;
    @SerializedName("Remark")
    @Expose
    private String remark;
    @SerializedName("Comment")
    @Expose
    private String comment;
    @SerializedName("Stage")
    @Expose
    private String stage;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Update")
    @Expose
    private String update;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("Status")
    @Expose
    private String status;


    public StockData(Parcel in) {
        month = in.readString();
        stockType = in.readString();
        source = in.readString();
        drumNo = in.readString();
        size = in.readString();
        date = in.readString();
        jobNo = in.readString();
        qtyMeter = in.readByte() == 0x00 ? null : in.readInt();
        srNo = in.readByte() == 0x00 ? null : in.readInt();
        condType = in.readString();
        remark = in.readString();
        comment = in.readString();
        stage = in.readString();
        location = in.readString();
        update = in.readString();
        username = in.readString();
        createdDate = in.readString();
        createdBy = in.readByte() == 0x00 ? null : in.readInt();
        status = in.readString();
    }

    public StockData() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getSrNo() {
        return srNo;
    }

    public void setSrNo(Integer srNo) {
        this.srNo = srNo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDrumNo() {
        return drumNo;
    }

    public void setDrumNo(String drumNo) {
        this.drumNo = drumNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public Integer getQtyMeter() {
        return qtyMeter;
    }

    public void setQtyMeter(Integer qtyMeter) {
        this.qtyMeter = qtyMeter;
    }

    public String getCondType() {
        return condType;
    }

    public void setCondType(String condType) {
        this.condType = condType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(month);
        dest.writeString(stockType);
        dest.writeString(source);
        dest.writeString(drumNo);
        dest.writeString(size);
        dest.writeString(date);
        dest.writeString(jobNo);
        if (qtyMeter == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(qtyMeter);
        }if (srNo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(srNo);
        }
        dest.writeString(condType);
        dest.writeString(remark);
        dest.writeString(comment);
        dest.writeString(stage);
        dest.writeString(location);
        dest.writeString(update);
        dest.writeString(username);
        dest.writeString(createdDate);
        if (createdBy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(createdBy);
        }
        dest.writeString(status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StockData> CREATOR = new Parcelable.Creator<StockData>() {
        @Override
        public StockData createFromParcel(Parcel in) {
            return new StockData(in);
        }

        @Override
        public StockData[] newArray(int size) {
            return new StockData[size];
        }
    };
}

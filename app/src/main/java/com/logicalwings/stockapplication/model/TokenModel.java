package com.logicalwings.stockapplication.model;

import com.google.gson.annotations.SerializedName;

public class TokenModel {

    @SerializedName("access_token")
    public String mAccessToken;

    @SerializedName("token_type")
    public String mTokenType;

    @SerializedName("expires_in")
    public Integer mExpiresIn;

    @SerializedName("userid")
    public String muserid;

    @SerializedName("userFullName")
    public String mUserFullName;

    @SerializedName("userName")
    public String userName;

    @SerializedName("IsAllowLoginAsUser")
    public String IsAllowLoginAsUser;

    @SerializedName("IsUploadPermission")
    public String IsUploadPermission;

    @SerializedName("LoginType")
    public String LoginType;

    @SerializedName("error_description")
    public String error_description;

    @SerializedName("error")
    public String error;

    @SerializedName(".issued")
    public String mIssued;

    @SerializedName(".expires")
    public String mExpires;

}

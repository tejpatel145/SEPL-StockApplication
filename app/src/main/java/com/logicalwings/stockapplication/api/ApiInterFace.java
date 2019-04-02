package com.logicalwings.stockapplication.api;

import com.logicalwings.stockapplication.model.BaseModel;
import com.logicalwings.stockapplication.model.RequestSearchData;
import com.logicalwings.stockapplication.model.ResponseSearchData;
import com.logicalwings.stockapplication.model.StockData;
import com.logicalwings.stockapplication.model.TokenModel;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiInterFace {

    @FormUrlEncoded
    @POST("AuthToken")
    Observable<TokenModel> loginUser(@Field("grant_type") String grantType,
                                     @Field("username") String inusername,
                                     @Field("password") String inPassword,
                                     @Field("logintype") String inlogintype);

    @POST("Stock/addOrUpdateStock")
    Observable<BaseModel> addOrUpdateStock(@Body StockData stockData);


    @POST("Stock/stockList")
    Observable<ResponseSearchData>getSearchList(@Body RequestSearchData requestSearchData);
}

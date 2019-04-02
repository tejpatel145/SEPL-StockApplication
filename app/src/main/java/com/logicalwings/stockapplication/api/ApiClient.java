package com.logicalwings.stockapplication.api;

import android.content.Context;
import android.util.Log;

import com.logicalwings.stockapplication.BuildConfig;
import com.logicalwings.stockapplication.utils.AppConstance;
import com.logicalwings.stockapplication.utils.PrefrenceConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static String BASE_URL = BuildConfig.URL;

    private static Retrofit retrofit = null;

    public static ApiInterFace getClient(final Context context) {

//        if (AppConstance.DEBUG)
//            Log.e(AppConstance.DEBUG_TAG, "getClient: "+ BASE_URL);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .writeTimeout(3000, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String authorizationHeaderTypt = "bearer";
                        String authorizationHeaderToken = PrefrenceConnector.readString(context,PrefrenceConnector.USER_TOKEN, "");

//                        if (AppConstance.DEBUG)
//                            Log.d(AppConstance.DEBUG_TAG, "intercept() called with: authorizationHeaderToken = [" + authorizationHeaderToken + "]");

                        request = request.newBuilder()
                                .addHeader("Authorization", authorizationHeaderTypt + " " + authorizationHeaderToken)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ApiInterFace.class);

    }

    private static class StringConverterFactory extends Converter.Factory {
        @Override
        public Converter<okhttp3.ResponseBody, String> responseBodyConverter(Type inType,
                                                                             Annotation[] inAnnotations,
                                                                             Retrofit inRetrofit) {
            if (String.class.equals(inType)) {
                return new Converter<okhttp3.ResponseBody, String>() {
                    @Override
                    public String convert(okhttp3.ResponseBody inValue) throws IOException {
                        return inValue.string().replace("\"", "");
                    }
                };
            }
            return null;
        }
    }
}

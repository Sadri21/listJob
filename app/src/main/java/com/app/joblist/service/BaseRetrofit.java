package com.app.joblist.service;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit {

   private static final String BASE_URL = "http://dev3.dansmultipro.co.id/api/";
   private static final int API_CONNECT_TIME_OUT = 60 * 10 * 1000;
   private static final int API_READ_TIME_OUT = 60 * 10 * 1000 ;
   private static Retrofit retrofitServer;

   public static Retrofit getRetrofitServer() {
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(interceptor);
      builder.readTimeout(API_READ_TIME_OUT, TimeUnit.MILLISECONDS);
      builder.connectTimeout(API_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
      OkHttpClient client = builder.build();

      if (retrofitServer == null) {
         retrofitServer = new Retrofit.Builder()
                 .baseUrl(BASE_URL)
                 .client(client)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
      }
      return retrofitServer;
   }
}

package com.example.chaitanya.helpchatinstagram.retrofit;

import com.example.chaitanya.helpchatinstagram.response.ResponseClass;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by chaitanya on 19/02/16.
 */
public interface RestInterface {


    @GET("/users/self")
    void showPopularMedia(@QueryMap Map<String,String> map, Callback<ResponseClass> responseClass);
}

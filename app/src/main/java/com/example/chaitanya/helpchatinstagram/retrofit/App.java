package com.example.chaitanya.helpchatinstagram.retrofit;

import android.app.Application;
import android.util.Log;

import retrofit.RestAdapter;

/**
 * Created by chaitanya on 19/02/16.
 */
public class App extends Application {
    private static RestInterface api;
    public static final String BASE_URL = "https://api.instagram.com/v1";

    @Override
    public void onCreate() {
        super.onCreate();
        api = new RestAdapter.Builder().setEndpoint(BASE_URL).build().create(RestInterface.class);
    }

    public static RestInterface getApi() {
        api = new RestAdapter.Builder().setEndpoint(BASE_URL).build().create(RestInterface.class);
        if(api==null){
            Log.d("API", "API IS NULL");
        }
        return api;
    }

    ;
}

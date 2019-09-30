package com.vila.testmobileintive.injection;

import android.app.Application;

public class MyApplication extends Application
{
    private RetrofitComponent retrofitComponent;


    @Override
    public void onCreate()
    {
        super.onCreate();

        retrofitComponent = DaggerRetrofitComponent.builder()
                .retrofitModule(new RetrofitModule(this))
                .build();
    }


    public RetrofitComponent getRetrofiComponent()
    {
        return retrofitComponent;
    }
}

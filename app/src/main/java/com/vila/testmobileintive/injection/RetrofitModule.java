package com.vila.testmobileintive.injection;

import android.app.Application;

import com.vila.testmobileintive.logic.Constantes;
import com.vila.testmobileintive.logic.RetrofitApi;
import com.vila.testmobileintive.logic.paging.PersonDataSourceFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class RetrofitModule
{
    Application application;

    public RetrofitModule(Application application)
    {
        this.application = application;
    }


    @Provides
    @Singleton
    HttpLoggingInterceptor providesLoggingInterceptor()
    {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    OkHttpClient providesHttpClient(HttpLoggingInterceptor httpLoggingInterceptor)
    {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(12, TimeUnit.SECONDS)
                .connectTimeout(12,TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient)
    {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(Constantes.DIRECCION_BASE_API)
                .build();
    }

    @Provides
    @Singleton
    RetrofitApi providesRetrofitApi(Retrofit retrofit)
    {
        return retrofit.create(RetrofitApi.class);
    }

    @Provides
    @Singleton
    PersonDataSourceFactory providesPersonDataSourceFactory()
    {
        return new PersonDataSourceFactory(application);
    }

}

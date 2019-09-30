package com.vila.testmobileintive.logic;

import com.vila.testmobileintive.model.Person;
import com.vila.testmobileintive.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi
{

    @GET("api/")
    Call<Result> getAllPersons
            (@Query("inc") String fields, @Query("seed")String seed,@Query("results")int persons);

    @GET("api/")
    Call<Result> getPersonByPage
            (@Query("inc") String fields, @Query("seed")String seed,
                    @Query("page") int page ,@Query("results")int persons);
}

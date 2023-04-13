package com.app.joblist.service;

import com.app.joblist.model.PositionModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
   @GET("recruitment/positions.json")
   Call<List<PositionModel>> getJobList(@Query("page") Integer page,
                                        @Query("description") String description,
                                        @Query("location") String location,
                                        @Query("full_time") Boolean fullTime);
   @GET("recruitment/positions/{id}")
   Call<PositionModel> getJobDetail(@Path("id") String id);
}
